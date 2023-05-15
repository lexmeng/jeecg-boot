package org.jeecg.modules.publishlist.tools;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JenkinsUtils {

    // 连接 Jenkins 需要设置的信息
    //"http://192.168.2.11:8080/jenkins/"
    private String JENKINS_URL;

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
    public void connectionUseRestful(String jobName, Map<String, String> paramMap)
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
            System.out.println("Response Code : " + responseCode);
        }catch (Exception e){
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

        utils.connectionUseRestful("devopsweb-manual-pr", map);
    }

}
