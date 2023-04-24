package org.jeecg.modules.devops.feishu;

import com.lark.oapi.Client;
import com.lark.oapi.core.response.BaseResponse;
import lombok.experimental.UtilityClass;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.config.thirdapp.ThirdAppTypeItemVo;

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
}
