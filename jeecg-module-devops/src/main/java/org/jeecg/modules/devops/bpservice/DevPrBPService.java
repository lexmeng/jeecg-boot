package org.jeecg.modules.devops.bpservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.devops.entity.DevPr;
import org.jeecg.modules.devops.exception.BussinessException;
import org.jeecg.modules.devops.logic.DevPrLogic;
import org.jeecg.modules.devops.service.IIssueService;
import org.jeecg.modules.devops.tools.IssueDevStatusResult;
import org.jeecg.modules.devops.domainservice.IIssueDomainService;
import org.jeecg.modules.devops.entity.Issue;
import org.jeecg.modules.devops.service.IDevPrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DevPrBPService {

    @Autowired
    private IIssueDomainService issueDomainService;

    @Autowired
    private IIssueService issueService;

    @Autowired
    private IDevPrService devPrService;

    /**
     * 从jira更新PR数据，如果为空则添加，如果不为空则先删除再添加。
     * 由于一个发布单的jiraVersionName是固定只有1个的，所以可以从页面传过来。
     * @param issueId
     * @param jiraVersionName
     */
    @Transactional
    public void updateFromJira(String issueId, String jiraVersionName, String projectId){
        //1 查询issue信息
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("issue_id", issueId);
        queryMap.put("jira_version_name",jiraVersionName);
        List<Issue> issueList = issueService.listByMap(queryMap);

        if(issueList==null || issueList.isEmpty()){
            throw new BussinessException("关联issue为空");
        }

        if(issueList.size() > 1){
            throw new BussinessException("关联issue大于1个");
        }

        Issue issue = issueList.get(0);

        //2 查询PR相关信息
        IssueDevStatusResult issueDevStatusResult = issueDomainService.fetchIssuePR(issue.getProjectId(), jiraVersionName, issueId);
        if(issueDevStatusResult.getPullRequests().size()==0){
            //throw new BussinessException("issue包含的pr数为空！");
            //issue包含的pr数为空！
            return;
        }

        List<DevPr> devPrList = new ArrayList<>();
        for(IssueDevStatusResult.PullRequest pr : issueDevStatusResult.getPullRequests()){
            DevPr devPr = DevPrLogic.convertJiraPrToDevPr(pr);
            devPr.setIssueId(issueId);
            devPr.setProjectId(projectId);
            devPr.setJiraVersionName(jiraVersionName);
            devPrList.add(devPr);
        }
        //3 查询数据库，如果没有则添加，如果有则更新
        Map<String,Object> prQueryMap = new HashMap<>();
        prQueryMap.put("project_id", projectId);
        prQueryMap.put("jira_version_name", jiraVersionName);
        prQueryMap.put("issue_id", issueId);

        List<DevPr> prList = devPrService.listByMap(prQueryMap);

        if(prList==null || prList.isEmpty()){
            devPrService.saveBatch(devPrList);
        }else{
            devPrService.removeByMap(prQueryMap);
            devPrService.saveBatch(devPrList);
        }
    }


    public List<DevPr> getIssuePr(String issueId, String jiraVersionName){
        //1 查询issue信息
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("issue_id", issueId);
        queryMap.put("jira_version_name",jiraVersionName);
        List<Issue> issueList = issueService.listByMap(queryMap);

        if(issueList==null || issueList.isEmpty()){
            throw new BussinessException("关联issue为空："+issueId);
        }

        if(issueList.size() > 1){
            throw new BussinessException("关联issue大于1个");
        }

        Issue issue = issueList.get(0);

        //2 查询devpr信息
        Map<String,Object> devPrQueryMap = new HashMap<>();
        devPrQueryMap.put("issue_id", issueId);
        devPrQueryMap.put("jira_version_name",jiraVersionName);

        List<DevPr> devPrList = devPrService.listByMap(devPrQueryMap);

        return devPrList;
    }

    public DevPr getPrFromJira(String issueId, String prId){
        //1 查询issue信息
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("issue_id", issueId);
        List<Issue> issueList = issueService.listByMap(queryMap);

        if(issueList==null || issueList.isEmpty()){
            throw new BussinessException("关联issue为空");
        }

        if(issueList.size() > 1){
            throw new BussinessException("关联issue大于1个");
        }

        Issue issue = issueList.get(0);

        //2 查询PR相关信息
        IssueDevStatusResult issueDevStatusResult = issueDomainService.fetchIssuePR(issue.getProjectId(), issue.getJiraVersionName(), issueId);
        if(issueDevStatusResult.getPullRequests().size()==0){
            throw new BussinessException("issue包含的pr数为空！");
        }

        List<DevPr> devPrList = new ArrayList<>();
        for(IssueDevStatusResult.PullRequest pr : issueDevStatusResult.getPullRequests()){
            DevPr devPr = DevPrLogic.convertJiraPrToDevPr(pr);
            devPr.setIssueId(issueId);
            devPrList.add(devPr);
        }

        //筛选出pr
        DevPr result=null;
        for(DevPr devPr : devPrList){
            if(devPr.getPrId().equals(prId)){
                result = devPr;
                break;
            }
        }
        return result;
    }


    public void updateByProjectlistId(String publishlistId){
        QueryWrapper<Issue> wrapper = new QueryWrapper<>();
        wrapper.eq("publishlist_id", publishlistId);
        List<Issue> issueList = issueService.list(wrapper);

        for(Issue issue : issueList){
            updateFromJira(issue.getIssueId(), issue.getJiraVersionName(), issue.getProjectId());
        }
    }
}
