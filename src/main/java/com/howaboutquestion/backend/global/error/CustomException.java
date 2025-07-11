package com.howaboutquestion.backend.global.error;

import com.howaboutquestion.backend.global.common.StatusCode;
import lombok.Getter;

/**
 * packageName    : com.howaboutquestion.backend.global.error<br>
 * fileName       : CustomException.java<br>
 * author         : eunchang <br>
 * date           : 2025-07-04<br>
 * description    : {@link RuntimeException}을 상속받아, 애플리케이션 전역에서 발생하는 일반 예외를 표현하는 클래스입니다<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-07-04          eunchang          최초생성<br>
 * 2025-07-09          eunchang          클래스명 수정<br>
 */
@Getter
public class CustomException extends RuntimeException {
    private final StatusCode errorCode;

    /**
     * 기본 INTERNAL_SERVER_ERROR 상태 CustomException 객체를 생성합니다.
     */
    public CustomException() {
        super(StatusCode.INTERNAL_SERVER_ERROR.getMessage());
        this.errorCode = StatusCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 기본 INTERNAL_SERVER_ERROR 상태와 커스텀 메시지를 함께 지정하여 CustomException 객체를 생성합니다.
     * @param message 커스텀 메시지
     */
    public CustomException(String message){
        super(message);
        this.errorCode = StatusCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 기본 INTERNAL_SERVER_ERROR 상태와 원인 예외를 함께 지정하여 생성합니다.
     * @param cause 원인 예외
     */
    public CustomException(Throwable cause){
        super(cause);
        this.errorCode = StatusCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 지정한 에러 코드와 함께 CustomException 객체를 생성합니다.
     * @param errorCode 예러 상태 코드
     */
    public CustomException(StatusCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 지정된 에러 코드와 사용자가 커스텀 메시지를 지정하여 CustomException 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     * @param message 커스텀 메시지
     */
    public CustomException(StatusCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

}
