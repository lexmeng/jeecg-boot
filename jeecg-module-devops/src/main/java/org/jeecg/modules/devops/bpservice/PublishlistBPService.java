package org.jeecg.modules.devops.bpservice;


import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.devops.entity.DependentComponent;
import org.jeecg.modules.devops.entity.Issue;
import org.jeecg.modules.devops.entity.PackageUrl;
import org.jeecg.modules.devops.entity.Project;
import org.jeecg.modules.devops.entity.Publishlist;
import org.jeecg.modules.devops.entity.PublishlistProject;
import org.jeecg.modules.devops.service.IIssueService;
import org.jeecg.modules.devops.service.IProjectService;
import org.jeecg.modules.devops.service.IPublishlistProjectService;
import org.jeecg.modules.devops.domainservice.IIssueDomainService;
import org.jeecg.modules.devops.domainservice.IPublishlistDomainService;
import org.jeecg.modules.devops.service.IPublishlistService;
import org.jeecg.modules.devops.entity.*;
import org.jeecg.modules.devops.exception.BussinessException;
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
    private ReleaseInfoBPService releaseInfoBPService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IssueBPService issueBPService;

    //新建发布单
    /*
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
        //releaseInfoBPService.updateReleaseInfo(publishlistId);
        issueBPService.generateEnAndChNameAndSave(publishlistId);

    }
*/

    public void savePublishlist(Publishlist publishlist, List<PublishlistProject> publishlistProjectList, List<DependentComponent> dependentComponentList, List<PackageUrl> packageUrlList){
        publishlistService.saveMain(publishlist, publishlistProjectList,dependentComponentList,packageUrlList);

        SpringContextUtils.getBean(PublishlistBPService.class).createIssueAndReleaseInfoAfterCreatePublishlist(publishlist);
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
        //releaseInfoBPService.updateReleaseInfo(publishlist.getId());
        issueBPService.generateEnAndChNameAndSave(publishlist.getId());
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

        SpringContextUtils.getBean(PublishlistBPService.class).publishUpdateIssueAndPublishlist(publishlistId, totalIssueList);

        //删除之前生成的release info信息
        /*
        Map<String,Object> map = new HashMap<>();
        map.put("publishlist_id",publishlistId);
        releaseInfoService.removeByMap(map);

        List<ReleaseInfo> releaseInfoList = new ArrayList<>();
        for(Issue issue: totalIssueList){
            //如果不需要生成发布信息，则跳过
            if(!releaseInfoDomainService.isNeedToGenerateReleaseInfo(issue.getIssueName(), publishlist.getProductLineName())){
                continue;
            }

            //if(issue.getIssueName().contains(Config.ISSUE_PUBLISH_FILTER_STRING)){
            //    continue;
            //}

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
        */

        return;
    }

    @Transactional(rollbackFor = Exception.class)
    public void publishUpdateIssueAndPublishlist(String publishlistId, List<Issue> totalIssueList){
        //初步判断状态
        if(publishlistDomainService.isPublished(publishlistId)){
            throw new BussinessException("发布单状态错误！发布单已发布");
        }

        issueDomainService.publish(publishlistId, totalIssueList);

        //记录发布时间，更新发布单状态
        publishlistDomainService.publish(publishlistId);

        //阶段二、生成releaseInfo信息
        if(!publishlistDomainService.isPublished(publishlistId)){
            throw new BussinessException("发布状态错误！");
        }

        //更新issue中英文名
        issueBPService.generateEnAndChNameAndSave(publishlistId);
    }


    @Transactional
    public void savePublishlist(List<Issue> issueList, Publishlist publishlist, List<PublishlistProject> publishlistProjectList, List<DependentComponent> dependentComponentList, List<PackageUrl> packageUrlList){
        //issueService.saveBatch(issueList);

        publishlistService.saveMain(publishlist, publishlistProjectList, dependentComponentList, packageUrlList);
    }
}
