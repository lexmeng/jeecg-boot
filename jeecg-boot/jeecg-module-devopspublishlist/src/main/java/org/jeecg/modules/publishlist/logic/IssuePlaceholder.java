package org.jeecg.modules.publishlist.logic;

import org.jeecg.modules.publishlist.entity.DependentComponent;
import org.jeecg.modules.publishlist.entity.Issue;
import org.jeecg.modules.publishlist.exception.BussinessException;

public class IssuePlaceholder implements PlaceholderAble<Issue>{

    public String getPlaceholderContent(String placeholder, Issue issue){
        Object result;
        if(placeholder.equals("${issueId}")){
            result = issue.getId();
        }else if(placeholder.equals("${issueNum}")){
            result = issue.getIssueNum();
        }else if(placeholder.equals("${issueName}")){
            result = issue.getIssueName();
        }else if(placeholder.equals("${issueType}")){
            result = issue.getIssueType();
        }else if(placeholder.equals("${issueLink}")){
            result = issue.getIssueLink();
        }
        /*else if(placeholder.equals("${issueJiraVersionName}")){
            result = issue.getJiraVersionName();
        }*/
        else if(placeholder.equals("${issueEnName}")){
            result = issue.getIssueEnName();
        }else if(placeholder.equals("${issueChName}")){
            result = issue.getIssueChName();
        }else{
            throw new BussinessException("issue占位符错误！");
        }

        return result.toString();
    }
}
