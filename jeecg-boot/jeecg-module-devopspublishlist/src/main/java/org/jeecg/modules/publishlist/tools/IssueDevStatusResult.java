package org.jeecg.modules.publishlist.tools;


import lombok.Data;

import java.util.List;

@Data
public class IssueDevStatusResult {


    @Data
    public static class Branch {
        private String name;
        private String url;
    }

    @Data
    public static class PullRequest {
        private String id;
        private String name;
        private String url;
        private String status;
    }

    private List<Branch> branches;
    private List<PullRequest> pullRequests;
}
