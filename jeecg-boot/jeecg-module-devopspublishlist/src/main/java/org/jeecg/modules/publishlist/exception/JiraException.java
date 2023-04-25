package org.jeecg.modules.publishlist.exception;

public class JiraException extends RuntimeException{
    public JiraException(){

    }

    public JiraException(String exceptionInfoStr){
        super(exceptionInfoStr);
    }
}
