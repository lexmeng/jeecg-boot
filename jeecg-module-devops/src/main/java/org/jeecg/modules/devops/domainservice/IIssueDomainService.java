package org.jeecg.modules.devops.domainservice;

import org.jeecg.modules.devops.entity.Issue;
import org.jeecg.modules.devops.tools.IssueDevStatusResult;

import java.util.List;

public interface IIssueDomainService {
    public void publish(String publishlistId, List<Issue> issueList);

    public List<Issue> getIssueListByProject(String publishlistId, String projectName, String jiraVersionName);

    IssueDevStatusResult fetchIssueDevStatus(String issueId);

    IssueDevStatusResult fetchIssuePR(String projectId, String jiraVersionName, String issueId);

    public void updateIssueList(String publishlistId, List<Issue> issueList);

    public void saveIssueListFirstTime(String publishlistId, List<Issue> issueList);

}
