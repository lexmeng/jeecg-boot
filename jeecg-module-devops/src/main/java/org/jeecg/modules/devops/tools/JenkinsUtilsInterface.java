package org.jeecg.modules.devops.tools;

import org.jeecg.modules.devops.vo.JenkinsJobResult;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.net.URI;

public interface JenkinsUtilsInterface {
    public ResponseEntity<String> buildWithParametersUseRestfulPost(String folderName, String jobName, MultiValueMap<String, String> paramMap);

    public ResponseEntity<String> buildWithPrarametersInKEUseRestfulPost(String jobName, MultiValueMap<String, String> paramMap);

    public JenkinsJobResult getJobResult(URI uri);

    public JenkinsJobResult retryGetJobResult(URI queueItemUri);
}
