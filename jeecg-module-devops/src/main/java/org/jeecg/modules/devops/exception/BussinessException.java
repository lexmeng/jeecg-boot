package org.jeecg.modules.devops.exception;

public class BussinessException extends RuntimeException{
    public BussinessException(){

    }

    public BussinessException(String exceptionInfoStr){
        super(exceptionInfoStr);
    }
}
