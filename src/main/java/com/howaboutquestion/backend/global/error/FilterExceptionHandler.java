package com.howaboutquestion.backend.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.howaboutquestion.backend.global.common.StatusCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * packageName    : com.howaboutquestion.backend.global.error<br>
 * fileName       : FilterErrorHandler.java<br>
 * author         : eunchang<br>
 * date           : 2025-07-04<br>
 * description    : 요청 처리 중 발생하는 예외를 가로채 공통 에러 응답을 반환하는 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang           최초생성<br>
 */
@Component
@RequiredArgsConstructor
public class FilterErrorHandler extends OncePerRequestFilter {
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 필터에서 발생하는 모든 예외를 처리
     * @param request HTTP 요청 정보
     * @param response HTTP 응답 객체
     * @param filterChain 다음 필터 또는 서블릿 호출 객체
     * @throws ServletException 서블릿 실행 중 예외
     * @throws IOException I/O 처리 중 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            setErrorResponse(response, e.getErrorCode());
        } catch (Exception e){
            setErrorResponse(response, StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 지정된 에러 코드에 따라 HTTP 응답을 구성하고 JSON 형태로 메시지 바디를 작성합니다.
     * @param response  응답 객체
     * @param errorCode 에러 코드 및 메시지
     * @throws IOException 메시지 바디 작성 중 I/O 예외
     */
    private void setErrorResponse(HttpServletResponse response, StatusCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        FailureResponseDTO errorBody = FailureResponseDTO.create(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorBody));
    }

}
