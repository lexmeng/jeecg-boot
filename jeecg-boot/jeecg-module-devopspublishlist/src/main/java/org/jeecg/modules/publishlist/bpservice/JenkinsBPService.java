package org.jeecg.modules.publishlist.bpservice;

import org.jeecg.modules.publishlist.tools.JenkinsOperateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JenkinsBPService {

    @Autowired
    private JenkinsOperateUtils jenkinsOperateUtils;

    public void jenkinsCommitProductPackagePR(String version, String content, String documentVersion){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("version", version);
        paramMap.put("content", content);
        paramMap.put("document_version", documentVersion);
        jenkinsOperateUtils.buildParamJob("devopsweb-productpackage-pr", paramMap);
    }

    public void jenkinsCommitProductHandbookPR(String version, String cnContent, String enContent, String documentVersion){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("version", version);
        paramMap.put("cn_content", cnContent);
        paramMap.put("en_content", enContent);
        paramMap.put("document_version", documentVersion);
        jenkinsOperateUtils.buildParamJob("devops_manual-pr", paramMap);
    }
}
