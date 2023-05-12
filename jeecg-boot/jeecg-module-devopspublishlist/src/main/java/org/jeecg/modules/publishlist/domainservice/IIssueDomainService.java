package org.jeecg.modules.publishlist.domainservice;

import org.jeecg.modules.publishlist.entity.Issue;

import java.util.List;

public interface IIssueDomainService {
    public void publish(String publishlistId, List<Issue> issueList);

    public List<Issue> getIssueListByProject(String publishlistId, String projectName, String jiraVersionName);

    public void updateIssueList(String publishlistId, List<Issue> issueList);

    public void saveIssueListFirstTime(String publishlistId, List<Issue> issueList);

}
