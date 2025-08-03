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


/**
 * packageName    : com.howaboutquestion.backend.global.common<br>
 * fileName       : LoggingAspect.java<br>
 * author         : cod0216 <br>
 * date           : 2025.08.03<br>
 * description    : 요청과 응답 수행 과정을 추적하는 로깅 Aspect 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.08.03          cod0216           최초 생성 <br>
 */

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final HttpServletRequest request;


    /**
     * 로그를 적용할 포인트컷을 정의합니다
     * `com.howaboutquestion.backend.domain.*.controller` 패키지의 모든 메서드 실행 시 적용됩니다.
     */
    @Pointcut("execution(* com.howaboutquestion.backend.domain.*.controller.*.*(..))")
    private void onRequest() {
    }

    /**
     * 로그를 적용할 포인트컷을 정의합니다
     * `com.howaboutquestion.backend.domain.*.service` 패키지의 모든 메서드 실행 시 적용됩니다.
     */
    @Pointcut("execution(* com.howaboutquestion.backend.domain.*.service.*.*(..))")
    private void onService() {
    }


    /**
     * 사용자의 요청에 대한 정보들을 로그에 기록합니다.
     * @param joinPoint 호출된 메서드의 정보를 제공하는 JoinPoint 객체
     */
    @Before("onRequest()")
    public void beforeRequestLog(JoinPoint joinPoint) {
        if(!log.isDebugEnabled()){
            log.info("[Request]-[{}] : HttpMethod: {} Url: {} Args: {}", getUserType(), request.getMethod(), request.getRequestURI(), getParams(joinPoint).toString());
        }else {
            log.debug("[Request]-[{}:{}] : HttpMethod: {} Url: {} Args: {} Headers: {}", getUserType(), getUserId(), request.getMethod(), request.getRequestURI(), getParams(joinPoint).toString(), getHeader(request));
        }
    }

    /**
     * 사용자의 요청에 의해 호출된 메서드가 응답을 반환하기 이전의 상태 정보를 로그에 기록합니다.
     * @param joinPoint 호출된 메서드의 정보를 제공하는 JoinPoint 객체
     */
    @Before("onService()")
    public void beforeServiceLog(JoinPoint joinPoint) {
        log.debug("[Before]-[{}:{}] : Method: {} Args: {}", getUserType(), getUserId(), joinPoint.getSignature().toShortString(), getParams(joinPoint));
    }

    /**
     * 사용자의 요청에 의해 호출된 메서드가 반환하는 정보들을 로그에 기록합니다.
     * @param joinPoint 호출된 메서드의 정보를 제공하는 JoinPoint 객체
     * @param returnObj 호출된 메서드의 결과값을 제공하는 returnObj 객체
     */
    @AfterReturning(value = "onService()", returning = "returnObj")
    public void afterServiceLog(JoinPoint joinPoint, Object returnObj) {
        log.debug("[After]-[{}:{}] : Method: {} Return: {}", getUserType(), getUserId(), joinPoint.getSignature().toShortString(), returnObj);
    }


    /**
     * 사용자의 요청에 의해 반환되는 결과를 로그에 기록합니다.
     * @param joinPoint 호출된 메서드의 정보를 제공하는 JoinPoint 객체
     * @param returnObj 호출된 메서드의 결과값을 제공하는 returnObj 객체
     */
    @AfterReturning(value = "onRequest()", returning = "returnObj")
    public void afterResponseLog(JoinPoint joinPoint, Object returnObj) {
        if(!log.isDebugEnabled())
            log.info("[Response]-[{}] : {}", getUserType(), returnObj);
    }

    /**
     * 사용자의 요청에대 한 최종 응답 정보들을 반환합니다.
     * @param proceed 실행 메서드
     * @return 수행한 메서드의 결과 값
     * @throws Throwable 메서드 수행 중 발생한 예러
     */
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

    /**
     * 메서드의 인자값에과 요청의 파라미터 값을 매핑하여 파마미터 정보를 생성합니다.
     * @param joinPoint 호출된 메서드의 정보를 제공하는 JoinPoint 객체
     * @return 매핑된 파라미터 정보
     */
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

    /**
     * 요청을 보낸 사용자의 UserType을 반환합니다.
     * @return 사용자의 타입
     */
    private String getUserType() {
        Authentication auth = SecurityContextHolder.getContext() != null ? SecurityContextHolder.getContext().getAuthentication() : null;

        if(Objects.isNull(auth) || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal()))
            return "ANONYMOUS";

        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .sorted()
                .collect(java.util.stream.Collectors.joining(","));
    }

    /**
     * 요청을 보낸 사용자의 Id값을 반환합니다.
     * @return 사용자의 Id값
     */
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

    /**
     * 사용자 요청에 포함되어 있는 Header 값들 반환합니다.
     * @param request 사용자 요청
     * @return 요청에 포함된 Header값
     */
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


