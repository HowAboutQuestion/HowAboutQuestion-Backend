package com.howaboutquestion.backend.global.error;


import com.howaboutquestion.backend.global.common.StatusCode;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * packageName    : com.howaboutquestion.backend.global.error<br>
 * fileName       : GlobalErrorHandler.java<br>
 * author         : eunchang<br>
 * date           : 2025-07-04<br>
 * description    :  전역에서 발생한 예외를 처리하는 GlobalErrorHandler 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang           최초생성<br>
 */
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * 검증 실패가 발생했을 때 호출됩니다.
     * @param e 발생한 예외
     * @param request 요청 정보
     * @return NVALID_PARAMETER 상태의 CustomException 응답
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> validationError(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, StatusCode.INVALID_PARAMETER, request);
    }

    /**
     * 비즈니스 로직에서 던진 GeneralError 예외를 처리합니다.
     * @param e 발생한 GeneralError
     * @param request 요청 정보
     * @return 예외에 매핑된 StatusCode를 사용한 CustomException 응답
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> generalError(CustomException e, WebRequest request){
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    /**
     * 선언된 예외를 제외한 모든 예외를 INTERNAL_SERVER_ERROR로 처리합니다.
     * @param e 발생한 예외
     * @param request 요청 정보
     * @return INTERNAL_SERVER_ERROR 상태의 CustomException 응답
     */
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<Object> forbiddenError(Exception e, WebRequest request){
        return handleExceptionInternal(e, StatusCode.NO_USER_PERMISSION, request);
    }

    /**
     * 선언된 예외를 제외한 모든 예외를 INTERNAL_SERVER_ERROR로 처리합니다.
     * @param e 발생한 예외
     * @param request 요청 정보
     * @return INTERNAL_SERVER_ERROR 상태의 CustomException 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e, WebRequest request){
        return handleExceptionInternal(e, StatusCode.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 오버로드된 헬퍼 메서드입니다.
     */
    private ResponseEntity<Object> handleExceptionInternal(Exception e, Object body,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request){
        return handleExceptionInternal(e,  StatusCode.valueOf(status), headers, status, request);
    }



    /**
     * ErrorCode와 함께 에러 응답을 구성하는 ErrorResponse를 담은 ResponseEntity를 반환하는 헬퍼 메서드입니다.
     */
    private ResponseEntity<Object> handleExceptionInternal(Exception e, StatusCode errorCode, WebRequest request){
        return handleExceptionInternal(e, FailureResponseDTO.create(errorCode,errorCode.getMessage(e)), HttpHeaders.EMPTY, errorCode.getStatus(), request);
    }

    /**
     * Spring의 기본 처리 메서드를 호출하여 ErrorResponse를 담은 ResponseEntity를 반환합니다.
     */
    private ResponseEntity<Object> handleExceptionInternal(Exception e, StatusCode errorCode, HttpHeaders headers, HttpStatus status, WebRequest request){
        return super.handleExceptionInternal(e, FailureResponseDTO.create(errorCode,errorCode.getMessage(e)), headers, status, request);
    }
}
