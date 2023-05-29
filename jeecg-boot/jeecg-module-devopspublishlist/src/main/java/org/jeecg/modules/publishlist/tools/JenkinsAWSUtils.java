package org.jeecg.modules.publishlist.tools;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.*;
import java.util.Base64;
import java.util.Map;


@Component
@Slf4j
public class JenkinsAWSUtils implements JenkinsUtilsInterface{

    // 连接 Jenkins 需要设置的信息
    //"http://192.168.2.11:8080/jenkins/"
    @Value("${jenkins.AWS.url}")
    private String JENKINS_URL;

    @Value("${jenkins.AWS.account}")
    private String JENKINS_USERNAME;

    @Value("${jenkins.AWS.token}")
    private String JENKINS_API_TOKEN;

    /**
     * Http 客户端工具
     *
     * 如果有些 API 该Jar工具包未提供，可以用此Http客户端操作远程接口，执行命令
     * @return
     */
    public JenkinsHttpClient getClient(){
        JenkinsHttpClient jenkinsHttpClient = null;
        try {
            jenkinsHttpClient = new JenkinsHttpClient(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_API_TOKEN);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsHttpClient;
    }

    /**
     * 连接 Jenkins
     */
    public JenkinsServer connection() {
        JenkinsServer jenkinsServer = null;
        try {
            jenkinsServer = new JenkinsServer(new URI(JENKINS_URL), JENKINS_USERNAME, JENKINS_API_TOKEN);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsServer;
    }

    private static URI addParameters(URI uri, Map<String, String> params) {
        String query = uri.getQuery();
        if (query == null) {
            query = "";
        } else {
            query += "&";
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            query += entry.getKey() + "=" + entry.getValue() + "&";
        }

        return uri.resolve("?" + query.substring(0, query.length() - 1));
    }

    public String build(String baseUrl, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append("?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue())).append("&");
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * 通过restful接口访问jenkins任务
     * 注：不适合参数长的情况，因为参数会放在url上，而url的长度是很有限的。
     * @param jobName
     * @param paramMap
     */
    public void buildWithParametersUseRestful(String jobName, Map<String, String> paramMap)
    {
        try{
            String urlStr = build(JENKINS_URL + "/DevOps" + "/job/" + jobName + "/buildWithParameters", paramMap);
            URL url = new URL(urlStr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // set the authentication header
            String userCredentials = JENKINS_USERNAME + ":" + JENKINS_API_TOKEN;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);

            // send the HTTP request
            int responseCode = connection.getResponseCode();
            log.info("Response Code : " + responseCode);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String generateFormData(Map<String, String> paramMap){
        StringBuilder stringBuilder = new StringBuilder();
        for(String str : paramMap.keySet()){
            if(!stringBuilder.toString().isEmpty()){
                stringBuilder.append("&");
            }
            stringBuilder.append(str);
            stringBuilder.append("=");
            stringBuilder.append(paramMap.get(str));
        }
        return stringBuilder.toString();
    }

    /*
    public void buildWithParametersUseRestfulPost(String jobName, Map<String, String> paramMap){
        try{
            URL url = new URL(JENKINS_URL + "/job/" + jobName + "/buildWithParameters");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // set the authentication header
            String userCredentials = JENKINS_USERNAME + ":" + JENKINS_API_TOKEN;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String data = generateFormData(paramMap);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            log.info("Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                if(inputStream != null){
                    log.info(inputStream.toString());
                    // 处理服务器返回的数据
                }
            }else{
                InputStream errorStream = connection.getErrorStream();
                if(errorStream != null){
                    log.info(errorStream.toString());
                }
                // 处理错误信息
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
*/
    public void buildWithParametersUseRestfulPost(String folderName, String jobName, MultiValueMap<String, String> paramMap){
        String userCredentials = JENKINS_USERNAME + ":" + JENKINS_API_TOKEN;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", basicAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramMap,headers);

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = JENKINS_URL + "job/" + folderName + "/job/" + jobName + "/buildWithParameters";

        log.info("buildWithParametersUseRestfulPost, apiUrl:"+apiUrl);
        for(String key : paramMap.keySet()){
            log.info("buildWithParametersUseRestfulPost, param key:"+key+"  value:"+paramMap.get(key));
        }

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        if(response.getStatusCodeValue() == 201){
            log.info("Jenkins job has been triggered successfully!");
        }else{
            log.info("Failed to trigger Jenkins Job.");
        }
    }

    public void buildWithPrarametersInKEUseRestfulPost(String jobName, MultiValueMap<String, String> paramMap){
        String userCredentials = JENKINS_USERNAME + ":" + JENKINS_API_TOKEN;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", basicAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramMap,headers);

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = JENKINS_URL + "job" + "/KE4" + "/job/" + jobName + "/buildWithParameters";
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        if(response.getStatusCodeValue() == 201){
            log.info("Jenkins job has been triggered successfully!");
        }else{
            log.info("Failed to trigger Jenkins Job.");
        }
    }


    public static void main(String[] args){
        JenkinsAWSUtils utils = new JenkinsAWSUtils();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        String cnContent = "aa";

        map.add("version", "4.5.1");
        map.add("cn_content", cnContent);
        map.add("en_content", "abcdefs");
        map.add("document_version", "4.5");

        utils.buildWithParametersUseRestfulPost("DevOps","devopsweb-manual-pr", map);
    }


}
