package org.jeecg.modules.publishlist.exception;

public class BussinessException extends RuntimeException{
    public BussinessException(){

    }

    public BussinessException(String exceptionInfoStr){
        super(exceptionInfoStr);
    }
}
