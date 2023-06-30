package org.jeecg.modules.devops.tools;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class IssueDevStatusResult {


    @Data
    public static class Branch {
        private String name;
        private String url;
    }

    @Data
    public static class BranchUrl{
        String branch;
        String url;
    }

    @Data
    public static class PullRequest {
        private String id;
        private String name;
        private Integer commentCount;
        private BranchUrl source;
        private BranchUrl destination;
        private String status;
        private String url;
        private Date lastaupdate;
    }

    private List<Branch> branches;
    private List<PullRequest> pullRequests;
}
