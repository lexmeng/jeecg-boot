package org.jeecg.modules.publishlist.bpservice;

import org.jeecg.modules.publishlist.entity.DevCiUtPr;
import org.jeecg.modules.publishlist.entity.DevPr;
import org.jeecg.modules.publishlist.entity.DevPrCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevPrCommitBPService {

    @Autowired
    private DevPrBPService devPrBPService;


    public DevPrCommit getCommitWhenPrUt(DevCiUtPr pr){
        DevPrCommit devPrCommit = new DevPrCommit();
        devPrCommit.setCommitId(pr.getPrbAcutalCommit());
        devPrCommit.setPrId(pr.getPrbPullId());
        devPrCommit.setIssueId(pr.getIssueNum());

        DevPr devPr = devPrBPService.getPrFromJira(pr.getIssueNum(), pr.getPrbPullId());

        devPrCommit.setPrName(devPr.getName());
        devPrCommit.setCommentCount(devPr.getCommentCount());
        devPrCommit.setSourceBranch(pr.getPrbSourceBranch());
        devPrCommit.setDestinationBranch(pr.getPrbTargetBranch());
        devPrCommit.setPrStatus(devPr.getPrStatus());
        devPrCommit.setPrUrl(devPr.getUrl());
        devPrCommit.setPrLastUpdate(devPr.getLastUpdate());

        return devPrCommit;
    }
}
