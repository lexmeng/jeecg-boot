package org.jeecg.modules.devops.domainservice.impl;

import org.jeecg.modules.devops.exception.StatusSwitchException;
import org.springframework.stereotype.Component;


@Component
public class PublishlistStatusMachine {

    private String initStatus = "DRAFT";
    private String developStatus = "DEVELOPING";

    private String testStatus = "TESTING";

    private String publishedStatus = "PUBLISHED";

    public String init(){
        return initStatus;
    }

    public String develop(String originStatus) throws StatusSwitchException {
        if(originStatus.equals(initStatus)){
            return developStatus;
        }else{
            String errorInfo = "状态切换异常!"+"从"+originStatus+"到"+developStatus;
            throw new StatusSwitchException(errorInfo);
        }
    }

    public String test(String originStatus) throws StatusSwitchException{
        if(originStatus.equals(developStatus)){
            return testStatus;
        }else{
            String errorInfo = "状态切换异常!"+"从"+originStatus+"到"+testStatus;
            throw new StatusSwitchException(errorInfo);
        }
    }

    public String publish(String originStatus) throws StatusSwitchException{
        if(originStatus.equals(testStatus)){
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

