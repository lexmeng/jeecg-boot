package org.jeecg.modules.publishlist.bpservice;


import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.domainservice.IIssueDomainService;
import org.jeecg.modules.publishlist.domainservice.IPublishlistDomainService;
import org.jeecg.modules.publishlist.domainservice.IReleaseInfoDomainService;
import org.jeecg.modules.publishlist.domainservice.impl.IssueDomainServiceImpl;
import org.jeecg.modules.publishlist.entity.*;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.service.*;
import org.jeecg.modules.publishlist.tools.IdTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ReleaseInfoBPService releaseInfoBPService;

    @Autowired
    private IProjectService projectService;

    //新建发布单
    public void createPublishlist(Publishlist publishlist, List<PublishlistProject> publishlistProjectList, List<DependentComponent> dependentComponentList, List<PackageUrl> packageUrlList){
        //生成publishlist存储的Id
        String publishlistId = IdTool.generalId();

        List<Issue> totalIssueList = new ArrayList<>();
        for(PublishlistProject publishlistProject : publishlistProjectList){
            Project project = projectService.getById(publishlistProject.getProjectId());
            List<Issue> issueList = issueDomainService.getIssueListByProject(publishlistId, project.getName(), publishlist.getJiraVersionName());
            totalIssueList.addAll(issueList);
        }

        //保存issue信息
        issueDomainService.saveIssueListFirstTime(publishlistId, totalIssueList);

        //根据输入信息，生成发布单
        savePublishlist(totalIssueList, publishlist, publishlistProjectList, dependentComponentList, packageUrlList);

        //更新releaseInfo信息
        releaseInfoBPService.updateReleaseInfo(publishlistId);

    }


    @Transactional
    public void savePublishlist(Publishlist publishlist, List<PublishlistProject> publishlistProjectList, List<DependentComponent> dependentComponentList, List<PackageUrl> packageUrlList){
        publishlistService.saveMain(publishlist, publishlistProjectList,dependentComponentList,packageUrlList);

        createIssueAndReleaseInfoAfterCreatePublishlist(publishlist);
    }

    @Transactional
    public void createIssueAndReleaseInfoAfterCreatePublishlist(Publishlist publishlist){

        List<PublishlistProject> publishlistProjectList = publishlistProjectService.selectByMainId(publishlist.getId());

        List<Issue> totalIssueList = new ArrayList<>();
        for(PublishlistProject publishlistProject : publishlistProjectList){
            Project project = projectService.getById(publishlistProject.getProjectId());
            List<Issue> issueList = issueDomainService.getIssueListByProject(publishlist.getId(), project.getName(), publishlist.getJiraVersionName());
            totalIssueList.addAll(issueList);
        }

        //保存issue信息
        issueDomainService.saveIssueListFirstTime(publishlist.getId(), totalIssueList);

        //更新releaseInfo信息
        releaseInfoBPService.updateReleaseInfo(publishlist.getId());
    }

    public void publish(String publishlistId){
        //阶段一。更新发布单和issue状态

        //读取发布单
        Publishlist publishlist = publishlistService.getById(publishlistId);
        List<PublishlistProject> projectList = publishlistProjectService.selectByMainId(publishlist.getId());

        //更新issue信息，现有issue存储issue history中
        List<Issue> totalIssueList = new ArrayList<>();
        for(PublishlistProject publishlistProject : projectList){
            Project project = projectService.getById(publishlistProject.getProjectId());
            List<Issue> issueList = issueDomainService.getIssueListByProject(publishlistId, project.getName(), publishlist.getJiraVersionName());
            totalIssueList.addAll(issueList);
        }
        issueDomainService.publish(publishlistId, totalIssueList);

        //记录发布时间，更新发布单状态
        publishlistDomainService.publish(publishlistId);

        //阶段二、生成releaseInfo信息
        if(!publishlistDomainService.isPublished(publishlistId)){
            throw new BussinessException("发布状态错误！");
        }

        //删除之前生成的release info信息
        Map<String,Object> map = new HashMap<>();
        map.put("publishlist_id",publishlistId);
        releaseInfoService.removeByMap(map);

        List<ReleaseInfo> releaseInfoList = new ArrayList<>();
        for(Issue issue: totalIssueList){
            //如果不需要生成发布信息，则跳过
            if(!releaseInfoDomainService.isNeedToGenerateReleaseInfo(issue.getIssueName(), publishlist.getProductLineName())){
                continue;
            }
            /*
            if(issue.getIssueName().contains(Config.ISSUE_PUBLISH_FILTER_STRING)){
                continue;
            }
            */
            ReleaseInfo releaseInfo;
            if(publishlist.getProductLineName().toUpperCase().contains("KE")){
                releaseInfo = releaseInfoDomainService.convertReleaseInfoFromIssue(issue, Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KE);
            }else if(publishlist.getProductLineName().toUpperCase().contains("KC")){
                releaseInfo = releaseInfoDomainService.convertReleaseInfoFromIssue(issue, Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KC);
            }else{
                throw new BussinessException("产品线名称错误！");
            }

            releaseInfoList.add(releaseInfo);
        }
        releaseInfoService.saveBatch(releaseInfoList);

        return;
    }


    @Transactional
    private void savePublishlist(List<Issue> issueList, Publishlist publishlist, List<PublishlistProject> publishlistProjectList, List<DependentComponent> dependentComponentList, List<PackageUrl> packageUrlList){
        //issueService.saveBatch(issueList);

        publishlistService.saveMain(publishlist, publishlistProjectList, dependentComponentList, packageUrlList);
    }
}
