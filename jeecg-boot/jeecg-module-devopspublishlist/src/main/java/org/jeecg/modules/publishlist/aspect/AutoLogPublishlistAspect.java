package org.jeecg.modules.publishlist.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class AutoLogPublishlistAspect {

    @Around("@annotation(org.jeecg.modules.publishlist.aspect.AutoLogPublishlist)")
    public Object autoLog(ProceedingJoinPoint joinPoint) throws Throwable{
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        AutoLogPublishlist autoLogAnnotation = method.getAnnotation(AutoLogPublishlist.class);
        String logMessage = autoLogAnnotation.value();

        Object[] args = joinPoint.getArgs();
        log.info("autoLog message:{}", logMessage);
        log.info("autoLog Begin execute method:{} from class:{} with arguments:{}", methodName, className, args);
        Object result = joinPoint.proceed();
        log.info("autoLog Finish execute method:{} from class:{} with result:{}", methodName, className, result);
        return result;
    }

}
