package org.jeecg.modules.publishlist.logic;

import org.jeecg.modules.publishlist.config.Config;

public class ReleaseInfoLogic {

    /**
     * 如果是kE产品线，不含分隔符也不需要生成
     * @param issueName
     * @param productLineName
     * @return
     */
    public static boolean isNeedToGenerateReleaseInfo(String issueName, String productLineName){
        if(issueName.contains(Config.ISSUE_PUBLISH_FILTER_STRING)){
            return false;
        }

        if(productLineName.toUpperCase().contains(Config.PRODUCT_LINE_NAME_KE)){
            if(!issueName.contains(Config.ISSUE_EN_AND_CH_SEPARATOR_IN_KE)){
                return false;
            }
        }

        return true;
    }
}
