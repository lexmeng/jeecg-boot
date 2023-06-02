package org.jeecg.modules.publishlist.tools;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import org.jeecg.modules.publishlist.vo.JenkinsJobResult;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Map;

public interface JenkinsUtilsInterface {
    public ResponseEntity<String> buildWithParametersUseRestfulPost(String folderName, String jobName, MultiValueMap<String, String> paramMap);

    public ResponseEntity<String> buildWithPrarametersInKEUseRestfulPost(String jobName, MultiValueMap<String, String> paramMap);

    public JenkinsJobResult getJobResult(URI uri);

    public JenkinsJobResult retryGetJobResult(URI queueItemUri);
}
