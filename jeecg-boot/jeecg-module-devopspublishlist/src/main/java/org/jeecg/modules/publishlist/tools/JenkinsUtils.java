package org.jeecg.modules.publishlist.tools;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.JobWithDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class JenkinsUtils {

    // 连接 Jenkins 需要设置的信息
    //"http://192.168.2.11:8080/jenkins/"
    private String JENKINS_URL="https://cicd-ofs.kyligence.com/job/DevOps";

    @Value("${jenkins.account}")
    private String JENKINS_USERNAME;

    @Value("${jenkins.password}")
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
            String urlStr = build(JENKINS_URL + "/job/" + jobName + "/buildWithParameters", paramMap);
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
                log.info(inputStream.toString());
                // 处理服务器返回的数据
            }else{
                InputStream errorStream = connection.getErrorStream();
                log.info(errorStream.toString());
                // 处理错误信息
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        JenkinsUtils utils = new JenkinsUtils();

        Map<String, String> map = new HashMap<>();
        map.put("version", "4.5.1");
        map.put("cn_content", "fdasasfsdaf");
        map.put("en_content", "abcdefs");
        map.put("document_version", "4.5");

        utils.buildWithParametersUseRestfulPost("devopsweb-manual-pr", map);
    }

}
