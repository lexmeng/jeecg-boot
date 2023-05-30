package org.jeecg.modules.publishlist.tools;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.publishlist.config.Config;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Component
@Slf4j
public class JenkinsUtils {
    @Autowired
    private JenkinsOFSUtils jenkinsOFSUtils;

    @Autowired
    private JenkinsAWSUtils jenkinsAWSUtils;

    public void buildWithParametersUseRestfulPost(String name, String folderName, String jobName, MultiValueMap<String, String> paramMap){
        if(name==null || name.isEmpty()){
            throw new BussinessException("Jenkins网络域为空，请填写OFS或AWS");
        }
        if(name.equals(Config.JENKINS_TYPE_OFS)){
            jenkinsOFSUtils.buildWithParametersUseRestfulPost(folderName, jobName, paramMap);
        }else if(name.equals(Config.JENKINS_TYPE_AWS))
        {
            jenkinsAWSUtils.buildWithParametersUseRestfulPost(folderName, jobName, paramMap);
        }else{
            throw new BussinessException("Jenkins网络域填写错误，请填写OFS或AWS");
        }
    }

    public void buildWithPrarametersInKEUseRestfulPost(String name, String jobName, MultiValueMap<String, String> paramMap){
        if(name==null || name.isEmpty()){
            throw new BussinessException("Jenkins网络域为空，请填写OFS或AWS");
        }
        if(name.equals(Config.JENKINS_TYPE_OFS)){
            jenkinsOFSUtils.buildWithPrarametersInKEUseRestfulPost(jobName, paramMap);
        }else if(name.equals(Config.JENKINS_TYPE_AWS))
        {
            jenkinsAWSUtils.buildWithPrarametersInKEUseRestfulPost(jobName, paramMap);
        }else{
            throw new BussinessException("Jenkins网络域填写错误，请填写OFS或AWS");
        }
    }


}
