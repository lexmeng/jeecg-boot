package org.jeecg.modules.publishlist.tools;


import com.alibaba.fastjson.JSONArray;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.atlassian.util.concurrent.Promise;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.exception.JiraException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class JiraClientUtils {
    private JiraRestClient jiraRestClient;
    private IssueRestClient issueClient;

    private ProjectRestClient projectRestClient;

    private IssueRestClient issueRestClient;

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

        projectRestClient = jiraRestClient.getProjectClient();

        issueRestClient = jiraRestClient.getIssueClient();
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

    /*public Issue getIssue(String issueKey){
        return issueClient.getIssue(issueKey).claim();
    }*/

    public SearchResult searchIssueByProjectAndFixVersions(String projectName, String fixVersions) throws JiraException{

        //String searchString = String.format("project = \"%s\" AND fixVersion in (\"%s\")", projectName, fixVersions);
        String searchString = "project = \"KE\"";
        int maxPerQuery = 500;
        int startIndex = 0;


        Promise<SearchResult> searchResult = jiraRestClient.getSearchClient().searchJql(searchString, maxPerQuery, startIndex, null);
        SearchResult results = searchResult.claim();

        //Project project = projectRestClient.getProject("KE").claim();

        //return null;
        return results;
    }

    /*
    public SearchResults searchJira(String projectName, String fixVersions) throws JqlParseException, SearchException {
        AsynchronousJiraRestClientFactory asyncClientFactory = new AsynchronousJiraRestClientFactory();
        jiraRestClient = asyncClientFactory.createWithBasicHttpAuthentication(
                URI.create("http://olapio.atlassian.net"), jiraAccountName, jiraSign
        );

        String searchString = String.format("project = \"%s\" AND fixVersion in (\"%s\")", projectName, fixVersions);
        ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        JqlQueryParser jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser.class);
        SearchService searchService = ComponentAccessor.getComponent(SearchService.class);

        Query query = jqlQueryParser.parseQuery(searchString);
        //SearchResults<com.atlassian.jira.issue.Issue> searchResults = searchService.search(currentUser, query, PagerFilter.getUnlimitedFilter());
        //return searchResults;
        return null;
    }
    */



    public void searchJqlGet() throws UnirestException{
        //RestTemplateBuilder builder = new RestTemplateBuilder();
        //RestTemplate restTemplate = builder.basicAuthentication(jiraAccountName,jiraSign).build();
        HttpResponse<JsonNode> response = Unirest.get("https://olapio.atlassian.net/rest/api/3/search")
                .basicAuth(jiraAccountName, jiraSign)
                .header("Accept", "application/json")
                .queryString("jql", "project = KE")
                .asJson();

        System.out.println(response.getBody());
    }

    private String delTab(String str){
        str = str.replace("\n", "").replace("\t", "");
        return str;
    }
    public IssueSearchResult restSearchIssueByProjectAndFixVersions(String projectName, String fixVersions) {
        String searchString = String.format("project = \"%s\" AND fixVersion in (\"%s\")", delTab(projectName), delTab(fixVersions));

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode payload = factory.objectNode();
        {
            ArrayNode expand = payload.putArray("expand");
            expand.add("names");
            expand.add("schema");
            expand.add("operations");
            ArrayNode fields = payload.putArray("fields");
            fields.add("summary");
            fields.add("status");
            fields.add("assignee");
            fields.add("project");
            fields.add("type");
            fields.add("issuetype");
            payload.put("fieldsByKeys", false);

            payload.put("jql", searchString);
            payload.put("maxResults", 500);
            payload.put("startAt", 0);
        }

        Unirest.setObjectMapper(new ObjectMapper(){
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        try{

            HttpResponse<JsonNode> response = Unirest.post("https://olapio.atlassian.net/rest/api/3/search")
                    .basicAuth(jiraAccountName, jiraSign)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .asJson();

            IssueSearchResult issueSearchResult = convertPostBodyToIssueSearchResult(response.getBody());

            return issueSearchResult;

        }catch (UnirestException e){
            e.printStackTrace();
            throw new RuntimeException("Jira系统访问产生错误: "+e.toString());
        }


    }

    private IssueSearchResult convertPostBodyToIssueSearchResult(JsonNode node){
        IssueSearchResult issueSearchResult = new IssueSearchResult();

        JSONObject jsonObject = node.getObject();

        issueSearchResult.setTotal(jsonObject.getInt("total"));
        issueSearchResult.setMaxResults(jsonObject.getInt("maxResults"));
        issueSearchResult.setStartAt(jsonObject.getInt("startAt"));

        org.json.JSONArray jsonArray = jsonObject.getJSONArray("issues");
        List<Issue> issueList = new ArrayList<>();
        for(Object object : jsonArray){
            JSONObject tempObject = (JSONObject) object;
            Issue issue = new Issue();
            issue.setIssueId(tempObject.getString("id"));
            String key = tempObject.getString("key");
            issue.setIssueNum(key);
            issue.setIssueName(tempObject.getJSONObject("fields").getString("summary"));
            issue.setIssueType(tempObject.getJSONObject("fields").getJSONObject("issuetype").getString("name"));
            issue.setIssueLink(String.format("https://olapio.atlassian.net/browse/%s", key));
            issue.setProjectId(tempObject.getJSONObject("fields").getJSONObject("project").getString("name"));

            issueList.add(issue);
            System.out.println(tempObject);
        }
        //List<Issue> issueList = JSONArray.parseArray(jsonObject.getJSONArray("issues").toString(), Issue.class);
        issueSearchResult.setIssues(issueList);
        return issueSearchResult;
    }



    public static void main(String[] args){
        JiraClientUtils utils = new JiraClientUtils();
        try{
            //utils.searchJira("Kyligence Enterprise","KE 4.6.6.0-GA 03/09");
            utils.initJiraRestClient();
            utils.searchIssueByProjectAndFixVersions("KE","KE 4.5.19.11-GA");
            //IssueSearchResult result =utils.restSearchIssueByProjectAndFixVersions("KE","KE 4.5.19.11-GA");
            //System.out.println(result);
        }
        finally{
            utils.closeJiraRestClient();
        }

    }
}
