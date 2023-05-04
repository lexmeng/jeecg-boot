package org.jeecg.modules.publishlist.tools;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JenkinsOperateUtils {

    // Jenkins 对象
    private JenkinsServer jenkinsServer;
    // http 客户端对象
    private JenkinsHttpClient jenkinsHttpClient;

    JenkinsOperateUtils(){
        // 连接 Jenkins
        jenkinsServer = JenkinsUtils.connection();
        // 设置客户端连接 Jenkins
        jenkinsHttpClient = JenkinsUtils.getClient();


    }

    public void getJob(){
        try{
            JobWithDetails job = jenkinsServer.getJob("devops_web_pr");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 执行无参数 Job build
     */
    public void buildJob(){
        try {
            jenkinsServer.getJob("test-job").build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行带参数 Job build
     */
    public void buildParamJob(){
        try {
            /**
             * 例如，现有一个job，拥有一个字符参数"key"
             * 现在对这个值进行设置，然后执行一个输出这个值的脚本
             */
            // 设置参数值
            Map<String,String> param = new HashMap<>();
            param.put("key","hello world!");
            // 执行 build 任务
            jenkinsServer.getJob("devops_web_pr").build(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
