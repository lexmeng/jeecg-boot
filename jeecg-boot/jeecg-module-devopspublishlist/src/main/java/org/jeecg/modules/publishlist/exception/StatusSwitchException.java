package org.jeecg.modules.publishlist.exception;

public class StatusSwitchException extends RuntimeException{
    public StatusSwitchException(){

    }

    public StatusSwitchException(String exceptionInfoStr){
        super(exceptionInfoStr);
    }
}
