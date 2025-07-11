package com.rajesh.url_shortener.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;
@Aspect
@Component
public class AspectClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(AspectClass.class);

    @Around("within(com.rajesh.url_shortener.*.*)")
    public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
            Object[] args = pjp.getArgs();

            LOGGER.info("Invoking method: {} with arguments: {}", methodName, Arrays.toString(args));
            long start = System.currentTimeMillis();
            try {
                Object result = pjp.proceed();
                long duration = System.currentTimeMillis() - start;
                LOGGER.info("Method {} returned: {} (Execution time: {} ms)", methodName, result, duration);
                return result;
            } catch (Throwable ex) {
                long duration = System.currentTimeMillis() - start;
                LOGGER.error("Method {} threw exception: {} (Execution time: {} ms)", methodName, ex.getMessage(), duration, ex);
                throw ex;
            }
    }
}
