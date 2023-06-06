package org.jeecg.modules.publishlist.bpservice;

import com.mchange.v2.collection.MapEntry;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.entity.TestQuardRequest;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.service.ITestQuardRequestService;
import org.jeecg.modules.publishlist.tools.IdTool;
import org.jeecg.modules.publishlist.tools.JenkinsOperateUtils;
import org.jeecg.modules.publishlist.tools.JenkinsUtils;
import org.jeecg.modules.publishlist.tools.ParamUtils;
import org.jeecg.modules.publishlist.vo.JenkinsJobResult;
import org.jeecg.modules.publishlist.vo.QuardTestParam;
import org.jeecg.modules.publishlist.vo.StepTestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.*;

@Service
public class JenkinsBPService {

    @Autowired
    private JenkinsOperateUtils jenkinsOperateUtils;

    @Autowired
    private JenkinsUtils jenkinsUtils;

    @Autowired
    private ITestQuardRequestService testQuardRequestService;

    public void jenkinsBuildWithParameters(String typeName, String folderName, String jobName, Map<String, String> paramMap){
        MultiValueMap<String, String> paramMultiValueMap = new LinkedMultiValueMap<>();
        for(String key : paramMap.keySet()){
            paramMultiValueMap.add(key, paramMap.get(key));
        }
        jenkinsUtils.buildWithParametersUseRestfulPost(typeName, folderName,jobName, paramMultiValueMap);
    }

    public void jenkinsCommitProductPackagePR(String version, String content, String documentVersion, String rowNumInsert){
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("row_num_insert", rowNumInsert);
        paramMap.add("version", version);
        paramMap.add("content", content);
        paramMap.add("document_version", documentVersion);
        //jenkinsOperateUtils.buildParamJob("devopsweb-productpackage-pr", paramMap);
        jenkinsUtils.buildWithParametersUseRestfulPost(Config.JENKINS_TYPE_OFS, "DevOps","devopsweb-productpackage-pr", paramMap);
    }

    public void jenkinsCommitProductHandbookPR(String version, String cnContent, String enContent, String documentVersion){
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("version", version);
        paramMap.add("cn_content", cnContent);
        paramMap.add("en_content", enContent);
        paramMap.add("document_version", documentVersion);
        //jenkinsOperateUtils.buildParamJob("devops_manual-pr", paramMap);
        jenkinsUtils.buildWithParametersUseRestfulPost(Config.JENKINS_TYPE_OFS,"DevOps","devopsweb-manual-pr", paramMap);
    }

    public void executeKE4StepTestJob(StepTestParam stepTestParam){
        stepTestParam.setFileName(ParamUtils.checkDefaultValue(stepTestParam.getFileName(), "KE-4.6.6.0-DEV-dbaec9f05f-c5dc9d997f.tar.gz"));

        ParamUtils.checkNotNull(stepTestParam.getIsCh());
        ParamUtils.checkNotNull(stepTestParam.getRedeployGw05());
        ParamUtils.checkNotNull(stepTestParam.getRedeployGw06());
        ParamUtils.checkNotNull(stepTestParam.getRedeployGw08());
        ParamUtils.checkNotNull(stepTestParam.getPlanName());
        ParamUtils.checkInEnum(stepTestParam.getProductline(), Arrays.asList("KE4X","CH"));
        stepTestParam.setEnvStage(ParamUtils.checkDefaultValue(stepTestParam.getEnvStage(),"RC"));
        stepTestParam.setCheckTime(ParamUtils.checkDefaultValue(stepTestParam.getCheckTime(), "600"));

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("file_name", stepTestParam.getFileName());
        paramMap.add("is_CH", Boolean.toString(stepTestParam.getIsCh()));
        paramMap.add("redeploy_gw05", Boolean.toString(stepTestParam.getRedeployGw05()));
        paramMap.add("redeploy_gw06", Boolean.toString(stepTestParam.getRedeployGw06()));
        paramMap.add("redeploy_gw08", Boolean.toString(stepTestParam.getRedeployGw08()));
        paramMap.add("plan_name", stepTestParam.getPlanName());
        paramMap.add("product_line", stepTestParam.getProductline());
        paramMap.add("version_name", stepTestParam.getVersionName());
        paramMap.add("envStage",stepTestParam.getEnvStage());
        paramMap.add("check_time", stepTestParam.getCheckTime());

        jenkinsUtils.buildWithPrarametersInKEUseRestfulPost(Config.JENKINS_TYPE_OFS, "0.KE-STEP", paramMap);
    }

    public Boolean executeKE4QuardTestJob(QuardTestParam quardTestParam){
        List<String> quardTypeEnum = Arrays.asList("KE4X_Daily", "KE4X_FULLCASE", "KE4X_COMPATIBILITY", "KE4X_UPGRADE");
        ParamUtils.checkInEnum(quardTestParam.getQuardType(), quardTypeEnum);

        List<String> platformEnum = Arrays.asList("ALL", "4X_CDH5.14_HA", "4X_CDH5.16_ALL", "4X_CDH5.8_ALL", "4X_CDH6.3.2_AZURE_ALL", "4X_CDH6.3.2_AZUREDEV_ALL", "4X_CDH7.1.6_ALL", "4X_FI-C90_RW", "4X_HADOOP2.7.2_ALL", "4X_HDP2.4_ALL", "4X_HDP3.1_ALL", "4X_MAPR6.1_ALL", "4X_MRS3.0.2_RW", "4X_TDH6.2.2_ALL", "4X_TDH6.2.2_THD");
        ParamUtils.checkInEnum(quardTestParam.getPlatform(), platformEnum);

        quardTestParam.setQuardRepo(ParamUtils.checkDefaultValue(quardTestParam.getQuardRepo(), "Kyligence"));
        quardTestParam.setQuardBranch(ParamUtils.checkDefaultValue(quardTestParam.getQuardBranch(), "quard-4.x"));
        quardTestParam.setPackageUrl(ParamUtils.checkDefaultValue(quardTestParam.getPackageUrl(), "https://package.kyligence.com/newten-cicd/artifacts/4.6.x/DEV/KE-4.6.6.0-DEV-dbaec9f05f-c5dc9d997f.tar.gz"));

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("quard_type", quardTestParam.getQuardType());
        paramMap.add("platform", quardTestParam.getPlatform());
        paramMap.add("quard_repo", quardTestParam.getQuardRepo());
        paramMap.add("quard_branch", quardTestParam.getQuardBranch());
        paramMap.add("reuse_num", quardTestParam.getReuseNum());
        paramMap.add("upgrade_num", quardTestParam.getUpgradeNum());
        paramMap.add("select_tests", quardTestParam.getSelectTests());
        paramMap.add("package_url", quardTestParam.getPackageUrl());

        ResponseEntity<String> response = jenkinsUtils.buildWithPrarametersInKEUseRestfulPost(Config.JENKINS_TYPE_OFS, "0.KE-QUARD", paramMap);
        URI queueItemUri = response.getHeaders().getLocation();
        JenkinsJobResult jobResult = jenkinsUtils.retryGetJobResult(Config.JENKINS_TYPE_OFS, queueItemUri);
        if(jobResult == null){
            throw new BussinessException("Jenkins job号获取超时！请手动写入job号");
        }

        TestQuardRequest quardRequest = new TestQuardRequest();

        quardRequest.setId(IdTool.generalId());
        quardRequest.setTestType(quardTestParam.getQuardType());
        quardRequest.setPlatformName(quardTestParam.getPlatform());
        quardRequest.setQuardRepo(quardTestParam.getQuardRepo());
        quardRequest.setQuardBranch(quardTestParam.getQuardBranch());
        quardRequest.setUpgradeFromNum(quardTestParam.getReuseNum());
        quardRequest.setUpgradeToNum(quardTestParam.getUpgradeNum());
        quardRequest.setSelectTests(quardTestParam.getSelectTests());
        quardRequest.setPackageUrl(quardTestParam.getPackageUrl());
        quardRequest.setJenkinsJobNum(jobResult.getNumber().toString());
        quardRequest.setJenkinsJobUrl(jobResult.getUrl());

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        quardRequest.setCreateBy(sysUser.getUsername());
        quardRequest.setCreateTime(new Date());

        Boolean result = testQuardRequestService.save(quardRequest);
        return result;
    }
}
