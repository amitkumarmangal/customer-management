package com.business.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* com.business..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getCanonicalName();
        String methodName = signature.getName();
        long start = System.currentTimeMillis();
        Logger logger=LoggerFactory.getLogger(className);
        logger.info("Going inside {}.{}()", className, methodName);
        logger.debug(" with args={} and headers are {}", Arrays.toString(joinPoint.getArgs()),getHeaders());
        try {
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - start;
            logger.info("Going outside from {}.{}() taking total time={}ms", className, methodName, timeTaken);
            logger.debug(" with response={} ", result);
            return result;
        } catch (Exception ex) {
            logger.error("Error Occurred in {}.{}() with message={}", className, methodName, ex.getMessage(), ex);
            throw ex;
        }
    }

    private Map<String,String> getHeaders() {
        Map<String, String> headers=new HashMap<>();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    headers.put(headerName, request.getHeader(headerName));
                }
            }
        }
        return headers;
    }
}