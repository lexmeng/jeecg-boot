package org.jeecg.modules.devops.tools;




import org.jeecg.modules.devops.entity.Issue;

import java.util.List;

public class IssueSearchResult {

    private Integer total;
    private Integer maxResults;

    private Integer startAt;

    private List<Issue> issues;


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }
}
