package org.jeecg.modules.devops.tools;

import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.response.BaseResponse;
import com.lark.oapi.service.im.v1.enums.ReceiveIdTypeEnum;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import lombok.experimental.UtilityClass;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.config.thirdapp.ThirdAppTypeItemVo;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;

import java.util.HashSet;
import java.util.Set;


@UtilityClass
public class FeiShuUtils {

    public static Client getClient() {
        final String appId = getConfig().getClientId();
        final String appSecret = getConfig().getClientSecret();

        return Client.newBuilder(appId, appSecret)
                .logReqAtDebug(true)
                .build();
    }

    public static ThirdAppTypeItemVo getConfig() {
        return SpringContextUtils.getBean(ThirdAppConfig.class).getFeishu();
    }

    public static <T> T getRespBody(BaseResponse<T> resp) {
        if (!resp.success()) {
            throw new JeecgBootException(resp.getError().toString());
        }

        return resp.getData();
    }

    public static void sendMessage(String tenantKey){

        try{
            getClient().im().message().create(
                    CreateMessageReq.newBuilder().receiveIdType(ReceiveIdTypeEnum.OPEN_ID.getValue()).createMessageReqBody(CreateMessageReqBody.newBuilder().content("你好！").build()).build(),
                    RequestOptions.newBuilder().tenantKey(tenantKey).build()
            );
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args){

        Set<String> set1 = new HashSet<>();
        set1.add("1");
        set1.add("2");

        Set<String> set2 = new HashSet<>();
        set2.add("2");
        set2.add("1");
        set2.add("3");

        System.out.println(set1.equals(set2));
    }
}
