package org.jeecg.modules.publishlist.bpservice;


import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.domainservice.IIssueDomainService;
import org.jeecg.modules.publishlist.domainservice.IPublishlistDomainService;
import org.jeecg.modules.publishlist.domainservice.IReleaseInfoDomainService;
import org.jeecg.modules.publishlist.domainservice.impl.IssueDomainServiceImpl;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.entity.ReleaseInfo;
import org.jeecg.modules.publishlist.service.IIssueService;
import org.jeecg.modules.publishlist.service.IPublishlistProjectService;
import org.jeecg.modules.publishlist.service.IPublishlistService;
import org.jeecg.modules.publishlist.service.IReleaseInfoService;
import org.jeecg.modules.publishlist.tools.IdTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublishlistBPService {
    @Autowired
    private IIssueService issueService;

    @Autowired
    private IIssueDomainService issueDomainService;

    @Autowired
    private IPublishlistProjectService publishlistProjectService;

    @Autowired
    private IPublishlistDomainService publishlistDomainService;

    @Autowired
    private IPublishlistService publishlistService;

    @Autowired
    private IReleaseInfoDomainService releaseInfoDomainService;

    @Autowired
    private IReleaseInfoService releaseInfoService;
    //新建发布单
    public void createPublishlist(Publishlist publishlist, List<PublishlistProject> publishlistProjectList){
        //生成publishlist存储的Id
        String publishlistId = IdTool.generalId();

        List<Issue> totalIssueList = new ArrayList<>();
        for(PublishlistProject publishlistProject : publishlistProjectList){
            List<Issue> issueList = issueDomainService.getIssueListByProject(publishlistId, publishlistProject.getProjectName(), publishlistProject.getJiraVersionName());
            totalIssueList.addAll(issueList);
        }

        //根据输入信息，生成发布单
        saveIssueListAndPublishlist(totalIssueList, publishlist, publishlistProjectList);

        //保存issue信息
        issueDomainService.saveIssueListFirstTime(publishlistId, totalIssueList);

    }

    public void publish(String publishlistId){
        //阶段一。更新发布单和issue状态

        //读取发布单
        Publishlist publishlist = publishlistService.getById(publishlistId);
        List<PublishlistProject> projectList = publishlistProjectService.selectByMainId(publishlist.getId());

        //更新issue信息，现有issue存储issue history中
        List<Issue> totalIssueList = new ArrayList<>();
        for(PublishlistProject publishlistProject : projectList){
            List<Issue> issueList = issueDomainService.getIssueListByProject(publishlistId, publishlistProject.getProjectName(), publishlistProject.getJiraVersionName());
            totalIssueList.addAll(issueList);
        }
        issueDomainService.publish(publishlistId, totalIssueList);

        //记录发布时间，更新发布单状态
        publishlistDomainService.publish(publishlistId);

        //阶段二、生成releaseInfo信息
        if(!publishlistDomainService.isPublished(publishlistId)){
            throw new RuntimeException("发布状态错误！");
        }

        List<ReleaseInfo> releaseInfoList = new ArrayList<>();
        for(Issue issue: totalIssueList){
            //如果不需要生成发布信息，则跳过
            if(issue.getIssueName().contains(Config.IssuePublishFilterString)){
                continue;
            }

            ReleaseInfo releaseInfo;
            if(publishlist.getProductLineName().toUpperCase().contains("KE")){
                releaseInfo = releaseInfoDomainService.convertReleaseInfoFromIssue(issue, Config.IssueEnAndChSeparatorInKE);
            }else if(publishlist.getProductLineName().toUpperCase().contains("KC")){
                releaseInfo = releaseInfoDomainService.convertReleaseInfoFromIssue(issue, Config.IssueEnAndChSeparatorInKC);
            }else{
                throw new RuntimeException("产品线名称错误！");
            }

            releaseInfoList.add(releaseInfo);
        }
        releaseInfoService.saveBatch(releaseInfoList);

        return;
    }


    @Transactional
    private void saveIssueListAndPublishlist(List<Issue> issueList, Publishlist publishlist, List<PublishlistProject> publishlistProjectList){
        issueService.saveBatch(issueList);

        publishlistService.saveMain(publishlist, publishlistProjectList);
    }
}
