package org.jeecg.modules.publishlist.tools;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.im.v1.model.*;
import java.util.HashMap;
import java.util.UUID;

import com.lark.oapi.core.request.RequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class FeishuMessageUtils {
    private static final long serialVersionUID = 1L;

    public static String  openIdLianfei = "ou_16983d1b6c11ee8018f1d895378d4656";
    public static String openIdJinYong = "ou_5a2ee67721da6095c303715c453e19e5";

    public static String openIdXinTao = "ou_412e5e6507a4105f757391352aafd1a4";

    @Value("${third-app.type.FEISHU-DEVOPSAPP.app-id}")
    public String appId;

    @Value("${third-app.type.FEISHU-DEVOPSAPP.app-secret}")
    public String getAppIdsecret;

    public static String tenantAccessToken = "";

    public static String getTenantAccessToken() {
        return tenantAccessToken;
    }

    public static void setTenantAccessToken(String tenantAccessToken) {
        FeishuMessageUtils.tenantAccessToken = tenantAccessToken;
    }

    /**
     * 发送飞书消息，用openIdLianfei、openIdJinYong、openIdXinTao来控制发送的对象
     * @param content
     * @throws Exception
     */
    public void sendFeiShuMsg(String content) {

        // 构建client
        Client client = Client.newBuilder(appId, getAppIdsecret)
                .disableTokenCache() //如需SDK自动管理租户Token的获取与刷新,可删除该行
                .build();

        String contentStr = String.format("{\"text\":\"%s\"}", content);
        // 创建请求对象
        CreateMessageReq req = CreateMessageReq.newBuilder()
                .receiveIdType("open_id")
                .createMessageReqBody(CreateMessageReqBody.newBuilder()
                        .receiveId(openIdJinYong)
                        .msgType("text")
                        .content(contentStr)
                        .uuid(UUID.randomUUID().toString())
                        .build())
                .build();

        if(tenantAccessToken.isEmpty()){
            tenantAccessToken = updateTalentAccessToken();
        }
        try{
            // 发起请求
            // 如开启了Sdk的token管理功能，就无需调用 RequestOptions.newBuilder().tenantAccessToken("t-xxx").build()来设置租户token了
            CreateMessageResp resp = client.im().message().create(req, RequestOptions.newBuilder()
                    .tenantAccessToken(tenantAccessToken)
                    .build());

            if(resp.getCode()==99991663){
                tenantAccessToken = updateTalentAccessToken();
                resp = client.im().message().create(req, RequestOptions.newBuilder()
                        .tenantAccessToken(tenantAccessToken)
                        .build());
            }

            // 处理服务端错误
            if(!resp.success()) {
                log.error(String.format("sendFeiShuMsg Error: code:%s,msg:%s,reqId:%s", resp.getCode(), resp.getMsg(), resp.getRequestId()));
                return;
            }

            // 业务数据处理
            //System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
        }catch (Exception e){
            e.printStackTrace();
            log.error("sendFeiShuMsg Error: "+e.getMessage());
        }

    }

    String url = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";
    public String updateTalentAccessToken(){
        String url = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"app_id\":\"%s\",\"app_secret\":\"%s\"}",appId,getAppIdsecret);
        HttpEntity<String> requstEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requstEntity, Result.class);

        if(!responseEntity.getBody().getCode().equals(0)){
            throw new BussinessException("更新tanant_access_key失败："+responseEntity.getBody().getMsg());
        }else{
            return responseEntity.getBody().getTenant_access_token();
        }
    }


}
