package org.jeecg.modules.publishlist.bpservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.domainservice.IIssueDomainService;
import org.jeecg.modules.publishlist.domainservice.IPublishlistDomainService;
import org.jeecg.modules.publishlist.domainservice.IReleaseInfoDomainService;
import org.jeecg.modules.publishlist.domainservice.impl.PublishlistDomainServiceImpl;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.Project;
import org.jeecg.modules.publishlist.entity.Publishlist;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.logic.ReleaseInfoLogic;
import org.jeecg.modules.publishlist.service.IIssueService;
import org.jeecg.modules.publishlist.service.IProjectService;
import org.jeecg.modules.publishlist.service.IPublishlistProjectService;
import org.jeecg.modules.publishlist.service.IPublishlistService;
import org.jeecg.modules.publishlist.tools.IssueDevStatusResult;
import org.jeecg.modules.publishlist.tools.JiraClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private IIssueService issueService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IPublishlistDomainService publishlistDomainService;


    public Boolean isEnableUpdateIssue(String publishlistId){
        if(publishlistDomainService.isPublished(publishlistId)){
            return false;
        }else{
            return true;
        }
    }
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
        //releaseInfoBPService.updateReleaseInfo(publishlistId, totalIssueList);
        SpringContextUtils.getBean(IssueBPService.class).generateEnAndChNameAndSave(publishlistId);
    }

    @Transactional
    public void generateEnAndChNameAndSave(String publishlistId){
        //1、先拉取需要写发布声明的issue列表
        List<Issue> issueList = getIssueListForRelease(publishlistId);

        //2、分解issue名
        Publishlist publishlist = publishlistService.getById(publishlistId);

        for(Issue issue : issueList){
            String issueChName;
            String issueEnName;
            String[] splitResult;

            if(!ReleaseInfoLogic.isNeedToGenerateReleaseInfo(issue.getIssueName(), publishlist.getProductLineName())){
                issue.setIssueChName("");
                issue.setIssueEnName("");
                issueService.updateById(issue);
                continue;
            }

            if(publishlist.getProductLineName().contains(Config.PRODUCT_LINE_NAME_KE)){
                splitResult = ReleaseInfoLogic.splitNameInfoFromIssue(issue, Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KE);
            }else if(publishlist.getProductLineName().contains(Config.PRODUCT_LINE_NAME_KC)){
                splitResult = ReleaseInfoLogic.splitNameInfoFromIssue(issue, Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KC);
            }else{
                throw new BussinessException("产品线名称错误！");
            }

            issueChName = splitResult[0];
            issueEnName = splitResult[1];
            issue.setIssueChName(issueChName);
            issue.setIssueEnName(issueEnName);
            issueService.updateById(issue);
        }

    }

    public IssueDevStatusResult fetchIssueDevStatus(String issueId){
        return issueDomainService.fetchIssueDevStatus(issueId);
    }

    public IssueDevStatusResult fetchIssuePR(String projectId, String jiraVersionName, String issueId){
        return issueDomainService.fetchIssuePR(projectId, jiraVersionName, issueId);
    }

    public List<Issue> getIssueListForRelease(String publishlistId){
        Publishlist publishlist = publishlistService.getById(publishlistId);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("publishlist_id",publishlistId);
        List<Issue> issueList = issueService.list(queryWrapper);

        List<Issue> resultIssueList = new ArrayList<>();
        for(Issue issue : issueList){
            if(ReleaseInfoLogic.isNeedToGenerateReleaseInfo(issue.getIssueName(), publishlist.getProductLineName())){
                resultIssueList.add(issue);
            }
        }
        return resultIssueList;
    }

    public List<Issue> getIssueListByPublishlistId(String publishlistId){
        Publishlist publishlist = publishlistService.getById(publishlistId);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("publishlist_id",publishlistId);
        List<Issue> issueList = issueService.list(queryWrapper);

        return issueList;
    }


    public List<Issue> getStoryIssueListForRelease(String publishlistId){
        Publishlist publishlist = publishlistService.getById(publishlistId);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("publishlist_id",publishlistId);
        List<Issue> issueList = issueService.list(queryWrapper);

        List<Issue> resultIssueList = new ArrayList<>();
        for(Issue issue : issueList){
            if(ReleaseInfoLogic.isNeedToGenerateReleaseInfo(issue.getIssueName(), publishlist.getProductLineName())){
                if(Config.ISSUE_TYPE_STORY.equals(issue.getIssueType().toLowerCase())){
                    resultIssueList.add(issue);
                }
            }
        }
        return resultIssueList;
    }

    public List<Issue> getBugIssueListForRelease(String publishlistId){
        Publishlist publishlist = publishlistService.getById(publishlistId);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("publishlist_id",publishlistId);
        List<Issue> issueList = issueService.list(queryWrapper);

        List<Issue> resultIssueList = new ArrayList<>();
        for(Issue issue : issueList){
            if(ReleaseInfoLogic.isNeedToGenerateReleaseInfo(issue.getIssueName(), publishlist.getProductLineName())){
                if(Config.ISSUE_TYPE_BUG.equals(issue.getIssueType().toLowerCase())){
                    resultIssueList.add(issue);
                }
            }
        }
        return resultIssueList;
    }
}
