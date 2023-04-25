package org.jeecg.modules.publishlist.domainservice.impl;

import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.domainservice.IIssueDomainService;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.entity.IssueHistory;
import org.jeecg.modules.publishlist.entity.PublishlistProject;
import org.jeecg.modules.publishlist.service.IIssueHistoryService;
import org.jeecg.modules.publishlist.service.IIssueService;
import org.jeecg.modules.publishlist.tools.JiraClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IssueDomainServiceImpl implements IIssueDomainService {

    @Autowired
    private IIssueService issueService;

    @Autowired
    private IIssueHistoryService issueHistoryService;

    @Autowired
    private JiraClientUtils jiraClientUtils;

    @Override
    @Transactional
    public void publish(String publishlistId, List<Issue> issueList){
        //读取发布单对应的issue信息
        QueryWrapper<Issue> wrapper = new QueryWrapper<>();
        wrapper.eq("publishlistId", publishlistId);
        List<Issue> oldIssueList = issueService.list(wrapper);

        //存储入history中
        List<IssueHistory> issueHistoryList = new ArrayList<>();
        for(Issue issue : oldIssueList){
            IssueHistory issueHistory = convertIssueToHistoryIssue(issue);
            issueHistoryList.add(issueHistory);
        }

        issueHistoryService.saveBatch(issueHistoryList);

        //删除原来的issue信息
        issueService.removeBatchByIds(issueList);

        //更新新的issue列表
        issueService.saveBatch(issueList);

    }


    //读取一个项目的issue列表，http操作不放入事务中
    @Override
    public List<Issue> getIssueListByProject(String publishlistId, String projectName, String jiraVersionName){
        List<Issue> issueList = new ArrayList<>();

        SearchResult searchResult = jiraClientUtils.searchIssueByProjectAndFixVersions(projectName, jiraVersionName);
        for(com.atlassian.jira.rest.client.api.domain.Issue issue: searchResult.getIssues()){
            Issue localIssue = convertIssueToLocalIssue(issue, publishlistId);
            issueList.add(localIssue);
        }

        return issueList;
    }


    private List<Issue> convertIssueListToLocalIssueList(List<com.atlassian.jira.rest.client.api.domain.Issue> issueList, String publishlistId){
        List<Issue> localIssueList = new ArrayList<>();
        for(com.atlassian.jira.rest.client.api.domain.Issue issue : issueList){
            Issue localIssue = convertIssueToLocalIssue(issue, publishlistId);
        }

        return localIssueList;
    }

    private Issue convertIssueToLocalIssue(com.atlassian.jira.rest.client.api.domain.Issue issue, String publishlistId) {
        Issue localIssue = new Issue();
        localIssue.setId(issue.getId().toString());
        localIssue.setIssueNum(issue.getKey());
        localIssue.setIssueName(issue.getSummary());
        localIssue.setIssueType(issue.getIssueType().getName());
        localIssue.setIssueLink(String.format("https://olapio.atlassian.net/browse/%s", issue.getKey()));
        localIssue.setPublishlistId(publishlistId);
        localIssue.setProjectId(issue.getProject().getName());
        localIssue.setCreateBy("");//todo
        localIssue.setCreateTime(new Date());
        return localIssue;
    }

    private IssueHistory convertIssueToHistoryIssue(Issue issue){
        IssueHistory issueHistory = new IssueHistory();
        issueHistory.setId(issue.getId());
        issueHistory.setIssueNum(issue.getIssueNum());
        issueHistory.setIssueName(issue.getIssueName());
        issueHistory.setIssueType(issue.getIssueType());
        issueHistory.setIssueLink(issue.getIssueLink());
        issueHistory.setProjectId(issue.getProjectId());
        issueHistory.setIssueCreateDatetime(issue.getCreateTime());
        issueHistory.setJiraVersionName(issue.getJiraVersionName());
        return issueHistory;
    }

    @Override
    public List<Issue> getIssueListForRelease(String publishlistId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("publishlist_id",publishlistId);
        List<Issue> issueList = issueService.list(queryWrapper);

        List<Issue> resultIssueList = new ArrayList<>();
        for(Issue issue : issueList){
            if(!issue.getIssueName().contains(Config.IssuePublishFilterString)){
                resultIssueList.add(issue);
            }
        }
        return resultIssueList;
    }


    public List<Issue> getStoryIssueListForRelease(String publishlistId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("publishlist_id",publishlistId);
        List<Issue> issueList = issueService.list(queryWrapper);

        List<Issue> resultIssueList = new ArrayList<>();
        for(Issue issue : issueList){
            if(!issue.getIssueName().contains(Config.IssuePublishFilterString) && issue.getIssueType().equals(Config.IssueTypeStory)){
                resultIssueList.add(issue);
            }
        }
        return resultIssueList;
    }

    public List<Issue> getBugIssueListForRelease(String publishlistId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("publishlist_id",publishlistId);
        List<Issue> issueList = issueService.list(queryWrapper);

        List<Issue> resultIssueList = new ArrayList<>();
        for(Issue issue : issueList){
            if(!issue.getIssueName().contains(Config.IssuePublishFilterString) && issue.getIssueType().equals(Config.IssueTypeBug)){
                resultIssueList.add(issue);
            }
        }
        return resultIssueList;
    }



}
