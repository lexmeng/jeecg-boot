package org.jeecg.modules.devops.exception;

public class JiraException extends RuntimeException{
    public JiraException(){

    }

    public JiraException(String exceptionInfoStr){
        super(exceptionInfoStr);
    }
}
