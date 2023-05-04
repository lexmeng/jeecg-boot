package org.jeecg.modules.publishlist.domainservice;

import org.jeecg.modules.publishlist.entity.Issue;

import java.util.List;

public interface IIssueDomainService {
    public void publish(String publishlistId, List<Issue> issueList);

    public List<Issue> getIssueListByProject(String publishlistId, String projectName, String jiraVersionName);


    public List<Issue> getIssueListForRelease(String publishlistId);

    public List<Issue> getStoryIssueListForRelease(String publishlistId);

    public List<Issue> getBugIssueListForRelease(String publishlistId);

    public void updateIssueList(String publishlistId, List<Issue> issueList);

    public void saveIssueListFirstTime(String publishlistId, List<Issue> issueList);

}
