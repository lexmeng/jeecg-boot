package org.jeecg.modules.publishlist.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.vo.JenkinsJobResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Map;

@Component
@Slf4j
public class JenkinsUtils {
    @Autowired
    private JenkinsOFSUtils jenkinsOFSUtils;

    @Autowired
    private JenkinsAWSUtils jenkinsAWSUtils;

    public ResponseEntity<String> buildWithParametersUseRestfulPost(String name, String folderName, String jobName, MultiValueMap<String, String> paramMap){
        if(name==null || name.isEmpty()){
            throw new BussinessException("Jenkins网络域为空，请填写OFS或AWS");
        }
        ResponseEntity<String> result;
        if(name.equals(Config.JENKINS_TYPE_OFS)){
            result = jenkinsOFSUtils.buildWithParametersUseRestfulPost(folderName, jobName, paramMap);
        }else if(name.equals(Config.JENKINS_TYPE_AWS))
        {
            result = jenkinsAWSUtils.buildWithParametersUseRestfulPost(folderName, jobName, paramMap);
        }else{
            throw new BussinessException("Jenkins网络域填写错误，请填写OFS或AWS");
        }
        return result;
    }

    public ResponseEntity<String> buildWithPrarametersInKEUseRestfulPost(String name, String jobName, MultiValueMap<String, String> paramMap){
        if(name==null || name.isEmpty()){
            throw new BussinessException("Jenkins网络域为空，请填写OFS或AWS");
        }
        ResponseEntity<String> result;
        if(name.equals(Config.JENKINS_TYPE_OFS)){
            result = jenkinsOFSUtils.buildWithPrarametersInKEUseRestfulPost(jobName, paramMap);
        }else if(name.equals(Config.JENKINS_TYPE_AWS))
        {
            result = jenkinsAWSUtils.buildWithPrarametersInKEUseRestfulPost(jobName, paramMap);
        }else{
            throw new BussinessException("Jenkins网络域填写错误，请填写OFS或AWS");
        }
        return result;
    }

    public JenkinsJobResult getJobResult(String name, URI uri){
        if(name==null || name.isEmpty()){
            throw new BussinessException("Jenkins网络域为空，请填写OFS或AWS");
        }
        JenkinsJobResult result;
        if(name.equals(Config.JENKINS_TYPE_OFS)){
            result = jenkinsOFSUtils.getJobResult(uri);
        }else if(name.equals(Config.JENKINS_TYPE_AWS))
        {
            result = jenkinsAWSUtils.getJobResult(uri);
        }else{
            throw new BussinessException("Jenkins网络域填写错误，请填写OFS或AWS");
        }
        return result;
    }

    public JenkinsJobResult retryGetJobResult(String name, URI queueItemUri){
        if(name==null || name.isEmpty()){
            throw new BussinessException("Jenkins网络域为空，请填写OFS或AWS");
        }
        JenkinsJobResult result;
        if(name.equals(Config.JENKINS_TYPE_OFS)){
            result = jenkinsOFSUtils.retryGetJobResult(queueItemUri);
        }else if(name.equals(Config.JENKINS_TYPE_AWS))
        {
            result = jenkinsAWSUtils.retryGetJobResult(queueItemUri);
        }else{
            throw new BussinessException("Jenkins网络域填写错误，请填写OFS或AWS");
        }
        return result;
    }


    public static void main(String[] args){
        String str = "{\"_class\":\"hudson.model.Queue$WaitingItem\",\"actions\":[{\"_class\":\"hudson.model.ParametersAction\",\"parameters\":[{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"version\",\"value\":\"4.5.1\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"cn_content\",\"value\":\"aa\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"en_content\",\"value\":\"abcdefs\"},{\"_class\":\"hudson.model.StringParameterValue\",\"name\":\"document_version\",\"value\":\"4.5\"}]},{\"_class\":\"hudson.model.CauseAction\",\"causes\":[{\"_class\":\"hudson.model.Cause$UserIdCause\",\"shortDescription\":\"Started by user Lianfei Qu\",\"userId\":\"lianfei.qu\",\"userName\":\"Lianfei Qu\"}]}],\"blocked\":false,\"buildable\":false,\"id\":20223,\"inQueueSince\":1685605376104,\"params\":\"\\u000aversion=4.5.1\\u000acn_content=aa\\u000aen_content=abcdefs\\u000adocument_version=4.5\",\"stuck\":false,\"task\":{\"_class\":\"org.jenkinsci.plugins.workflow.job.WorkflowJob\",\"name\":\"devopsweb-manual-pr\",\"url\":\"https://cicd-ofs.kyligence.com/job/PM/job/devopsweb-manual-pr/\",\"color\":\"red_anime\"},\"url\":\"queue/item/20223/\",\"why\":\"Finished waiting\",\"timestamp\":1685605381104}";
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(JSON.parse(str));
        String url = jsonObject.getString("url");
        String aa = jsonObject.getString("executable");
        JSONObject obj = jsonObject.getJSONObject("task");
        String taskUrl = obj.getString("url");
        log.info(taskUrl);
    }


}
