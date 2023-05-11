package org.jeecg.modules.publishlist.bpservice;

import org.jeecg.modules.publishlist.tools.JenkinsOperateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JenkinsBPService {

    @Autowired
    private JenkinsOperateUtils jenkinsOperateUtils;

    public void jenkinsCommitProductPackagePR(){
        jenkinsOperateUtils.buildJob();
    }

    public void jenkinsCommitProductHandbookPR(){
        jenkinsOperateUtils.buildParamJob();
    }
}
