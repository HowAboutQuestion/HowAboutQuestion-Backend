package com.howaboutquestion.backend.global.error;

import com.howaboutquestion.backend.global.common.StatusCode;
import lombok.Getter;

/**
 * packageName    : com.howaboutquestion.backend.global.error<br>
 * fileName       : Error.java<br>
 * author         : eunchang <br>
 * date           : 2025-07-04<br>
 * description    : {@link RuntimeException}을 상속받아, 애플리케이션 전역에서 발생하는 일반 예외를 표현하는 클래스입니다<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-07-04          eunchang          최초생성<br>
 */
@Getter
public class Error extends RuntimeException {
    private final StatusCode errorCode;

    /**
     * 기본 INTERNAL_SERVER_ERROR 상태 Error 객체를 생성합니다.
     */
    public Error() {
        super(StatusCode.INTERNAL_SERVER_ERROR.getMessage());
        this.errorCode = StatusCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 기본 INTERNAL_SERVER_ERROR 상태와 커스텀 메시지를 함께 지정하여 Error 객체를 생성합니다.
     * @param message 커스텀 메시지
     */
    public Error(String message){
        super(StatusCode.INTERNAL_SERVER_ERROR.getMessage(message));
        this.errorCode = StatusCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 기본 INTERNAL_SERVER_ERROR 상태와 커스텀 메시지, 원인 예외를 함께 지정하여 Error 객체를 생성합니다.
     * @param message 커스텀 메시지
     * @param cause 원인 예외
     */
    public Error(String message, Throwable cause){
        super(StatusCode.INTERNAL_SERVER_ERROR.getMessage(message), cause);
        this.errorCode = StatusCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 기본 INTERNAL_SERVER_ERROR 상태와 원인 예외를 함께 지정하여 생성합니다.
     * @param cause 원인 예외
     */
    public Error(Throwable cause){
        super(StatusCode.INTERNAL_SERVER_ERROR.getMessage(cause));
        this.errorCode = StatusCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 지정한 에러 코드와 함께 Error 객체를 생성합니다.
     * @param errorCode 예러 상태 코드
     */
    public Error(StatusCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 지정된 에러 코드와 사용자가 커스텀 메시지를 지정하여 Error 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     * @param message 커스텀 메시지
     */
    public Error(StatusCode errorCode, String message){
        super(errorCode.getMessage(message));
        this.errorCode = errorCode;
    }

    /**
     * 지정된 에러 코드와 사용자가 커스텀 메시지, 원인 예외를 함께 지정하여 Error 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     * @param message 커스텀 메시지
     * @param cause 원인 예외
     */
    public Error(StatusCode errorCode, String message, Throwable cause){
        super(errorCode.getMessage(message), cause);
        this.errorCode = errorCode;
    }

    /**
     * 지정된 에러 코드와 원인 예외를 함께 지정하여 Error 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     * @param cause 원인 예외
     */
    public Error(StatusCode errorCode, Throwable cause){
        super(errorCode.getMessage(cause), cause);
        this.errorCode = errorCode;
    }

}
