package com.howaboutquestion.backend.global.common;

import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final HttpServletRequest request;


    @Pointcut("execution(* com.howaboutquestion.backend.domain.*.controller.*.*(..))")
    private void onRequest() {
    }

    @Pointcut("execution(* com.howaboutquestion.backend.domain.*.service.*.*(..))")
    private void onService() {
    }

    @Before("onRequest()")
    public void beforeRequestLog(JoinPoint joinPoint) {
        if(!log.isDebugEnabled()){
            log.info("[Request]-[{}] : HttpMethod: {} Url: {} Args: {}", getUserType(), request.getMethod(), request.getRequestURI(), getParams(joinPoint).toString());
        }else {
            log.debug("[Request]-[{}:{}] : HttpMethod: {} Url: {} Args: {} Headers: {}", getUserType(), getUserId(), request.getMethod(), request.getRequestURI(), getParams(joinPoint).toString(), getHeader(request));
        }
    }

    @Before("onService()")
    public void beforeServiceLog(JoinPoint joinPoint) {
        log.debug("[Before]-[{}:{}] : Method: {} Args: {}", getUserType(), getUserId(), joinPoint.getSignature().toShortString(), getParams(joinPoint));
    }

    @AfterReturning(value = "onService()", returning = "returnObj")
    public void afterServiceLog(JoinPoint joinPoint, Object returnObj) {
        log.debug("[After]-[{}:{}] : Method: {} Return: {}", getUserType(), getUserId(), joinPoint.getSignature().toShortString(), returnObj);
    }


    @AfterReturning(value = "onRequest()", returning = "returnObj")
    public void afterResponseLog(JoinPoint joinPoint, Object returnObj) {
        if(!log.isDebugEnabled())
            log.info("[Response]-[{}] : {}", getUserType(), returnObj);
    }

    @Around("onRequest()")
    public Object aroundRequestLog(ProceedingJoinPoint proceed) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceed.proceed();
        long duration = System.currentTimeMillis() - start;
        log.debug("[Response]-[{}:{}] : Method: {} time: {}ms, returned: {} ",
                getUserType(),
                getUserId(),
                proceed.getSignature().toShortString(),
                duration,
                result
        );
        return result;

    }

    private Map<String, Object> getParams(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }

    private String getUserType() {
        Authentication auth = SecurityContextHolder.getContext() != null ? SecurityContextHolder.getContext().getAuthentication() : null;

        if(Objects.isNull(auth) || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal()))
            return "ANONYMOUS";

        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .sorted()
                .collect(java.util.stream.Collectors.joining(","));
    }

    private String getUserId() {
        Authentication auth = SecurityContextHolder.getContext() != null ? SecurityContextHolder.getContext().getAuthentication() : null;

        if(Objects.isNull(auth) || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal()))
            return "X";

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetail userDetail) {
            return String.valueOf(userDetail.getUserId());
        }
        return auth.getName();
    }

    private Map<String, String> getHeader(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers;
    }
}


