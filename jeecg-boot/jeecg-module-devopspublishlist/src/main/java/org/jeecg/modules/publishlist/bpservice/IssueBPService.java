package org.jeecg.modules.publishlist.bpservice;

import org.jeecg.modules.publishlist.domainservice.IIssueDomainService;
import org.jeecg.modules.publishlist.domainservice.IPublishlistDomainService;
import org.jeecg.modules.publishlist.domainservice.impl.PublishlistDomainServiceImpl;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.Project;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.service.IProjectService;
import org.jeecg.modules.publishlist.service.IPublishlistProjectService;
import org.jeecg.modules.publishlist.service.IPublishlistService;
import org.jeecg.modules.publishlist.tools.JiraClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IssueBPService {

    @Autowired
    private JiraClientUtils jiraClientUtils;

    @Autowired
    private IPublishlistService publishlistService;

    @Autowired
    private IPublishlistProjectService publishlistProjectService;

    @Autowired
    private IIssueDomainService issueDomainService;

    @Autowired
    private ReleaseInfoBPService releaseInfoBPService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IPublishlistDomainService publishlistDomainService;


    //更新issue列表的业务流程
    public void updateIssueList(String publishlistId){
        if(publishlistDomainService.isPublished(publishlistId)){
            throw new BussinessException("发布单已经发布，不允许更新issue！");
        }

        //1、先拉取新的issue列表
        //读取发布单
        Publishlist publishlist = publishlistService.getById(publishlistId);
        List<PublishlistProject> projectList = publishlistProjectService.selectByMainId(publishlist.getId());

        //更新issue信息
        List<Issue> totalIssueList = new ArrayList<>();
        for(PublishlistProject publishlistProject : projectList){
            Project project = projectService.getById(publishlistProject.getProjectId());
            List<Issue> issueList = issueDomainService.getIssueListByProject(publishlistId, project.getName(), publishlist.getJiraVersionName());
            totalIssueList.addAll(issueList);
        }

        //2、删除旧的issue列表，非首次，issue列表里必然是有的。然后插入新的issue列表
        issueDomainService.updateIssueList(publishlistId, totalIssueList);

        //3、更新releaseInfo
        releaseInfoBPService.updateReleaseInfo(publishlistId, totalIssueList);
    }
}
