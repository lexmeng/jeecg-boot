package org.jeecg.modules.publishlist.domainservice.impl;

import org.jeecg.modules.publishlist.exception.StatusSwitchException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class PublishlistStatusMachine {

    private String initStatus = "INIT";
    private String publishedStatus = "PUBLISHED";

    public String init(){
        return initStatus;
    }

    public String publish(String originStatus) throws StatusSwitchException{
        if(originStatus.equals(initStatus)){
            return publishedStatus;
        }else{
            String errorInfo = "状态切换异常!"+"从"+originStatus+"到"+publishedStatus;
            throw new StatusSwitchException(errorInfo);
        }
    }

    public Boolean isPublished(String stage){
        if(stage.equals(publishedStatus)){
            return true;
        }else{
            return false;
        }
    }
}

