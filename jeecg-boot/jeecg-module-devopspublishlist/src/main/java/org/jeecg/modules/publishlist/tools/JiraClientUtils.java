package org.jeecg.modules.publishlist.tools;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import io.atlassian.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;

import java.io.IOException;
import org.jeecg.modules.publishlist.exception.JiraException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JiraClientUtils {
    private JiraRestClient jiraRestClient;
    private IssueRestClient issueClient;

    @Value("${jira.account-name}")
    private String jiraAccountName;

    @Value("${jira.sign}")
    private String jiraSign;

    @PostConstruct
    public void initJiraRestClient(){
        AsynchronousJiraRestClientFactory asyncClientFactory = new AsynchronousJiraRestClientFactory();
        jiraRestClient = asyncClientFactory.createWithBasicHttpAuthentication(
                URI.create("http://olapio.atlassian.net"), jiraAccountName, jiraSign
        );

        issueClient = jiraRestClient.getIssueClient();
    }

    @PreDestroy
    public void closeJiraRestClient() {
        try {
            if (jiraRestClient != null) {
                jiraRestClient.close();
            }
        } catch (IOException e) {
            log.error("closeJiraRestClient 异常: ");
        }
    }

    public Issue getIssue(String issueKey){
        return issueClient.getIssue(issueKey).claim();
    }

    public SearchResult searchIssueByProjectAndFixVersions(String projectName, String fixVersions) throws JiraException{
        String searchString = String.format("project = %s AND fixVersion in (%s)", projectName, fixVersions);
        int maxPerQuery = 500;
        int startIndex = 0;
        Promise<SearchResult> searchResult = jiraRestClient.getSearchClient().searchJql(searchString, maxPerQuery, startIndex, null);
        SearchResult results = searchResult.claim();
        return results;
    }

    public static void main(String[] args){
        JiraClientUtils utils = new JiraClientUtils();
        try{
            utils.initJiraRestClient();
            utils.searchIssueByProjectAndFixVersions("\"Kyligence Enterprise\"","\"KE 4.6.6.0-GA 03/09\"");
        }finally{
            utils.closeJiraRestClient();
        }

    }
}
