package org.jeecg.modules.devops.exception;

public class StatusSwitchException extends RuntimeException{
    public StatusSwitchException(){

    }

    public StatusSwitchException(String exceptionInfoStr){
        super(exceptionInfoStr);
    }
}
