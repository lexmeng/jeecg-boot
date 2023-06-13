package org.jeecg.modules.publishlist.logic;

import org.jeecg.modules.publishlist.entity.DevPr;
import org.jeecg.modules.publishlist.tools.IssueDevStatusResult;

public class DevPrLogic {

    public static DevPr convertJiraPrToDevPr(IssueDevStatusResult.PullRequest pullRequest){
        DevPr devPr = new DevPr();
        devPr.setPrId(pullRequest.getId());
        devPr.setName(pullRequest.getName());
        devPr.setCommentCount(pullRequest.getCommentCount());
        devPr.setSourceBranch(pullRequest.getSource().getBranch());
        devPr.setDestinationBranch(pullRequest.getDestination().getBranch());
        devPr.setPrStatus(pullRequest.getStatus());
        devPr.setUrl(pullRequest.getUrl());
        devPr.setLastUpdate(pullRequest.getLastaupdate());
        return devPr;
    }
}
