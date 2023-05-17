package org.jeecg.modules.publishlist.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* org.jeecg.modules.publishlist..*(..))")
    public void mapping(){

    }

    @Around("mapping()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Begin execute method:{} from class:{} with arguments:{}", methodName, className, args);
        Object result = joinPoint.proceed();
        log.info("Finish execute method:{} from class:{} with result:{}", methodName, className, result);
        return result;
    }

    /*
    @Around("executeService()")
    public Object doAround() throws Throwable{

    }
    */

}
