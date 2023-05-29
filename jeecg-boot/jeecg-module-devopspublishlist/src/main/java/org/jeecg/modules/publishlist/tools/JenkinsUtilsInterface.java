package org.jeecg.modules.publishlist.tools;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface JenkinsUtilsInterface {
    public void buildWithParametersUseRestfulPost(String folderName, String jobName, MultiValueMap<String, String> paramMap);

    public void buildWithPrarametersInKEUseRestfulPost(String jobName, MultiValueMap<String, String> paramMap);
}
