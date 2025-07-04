package com.howaboutquestion.backend.global.error;

import com.howaboutquestion.backend.global.common.ResponseMetadata;
import com.howaboutquestion.backend.global.common.StatusCode;
import lombok.*;

/**
 * packageName    : com.howaboutquestion.backend.global.error<br>
 * fileName       : ErrorResponse.java<br>
 * author         : eunchang <br>
 * date           : 2025-07-04<br>
 * description    : {@link ResponseMetadata}를 상속받은 ErrorResponse 를 반환하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang          최초생성<br>
 */

@Getter
@Setter
public class ErrorResponse extends ResponseMetadata {

    /**
     * 반환할 ErrorCode와 함께 ErrorResponse 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     */
    private ErrorResponse(StatusCode errorCode) {
        super(false, errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 반환할 ErrorCode와 Exception 메시지와 함께 ErrorResponse 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     * @param e 발생한 예외
     */
    private ErrorResponse(StatusCode errorCode, Exception e) {
        super(false, errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage(e));
    }

    /**
     * 반환할 ErrorCode와 작성한 메시지와 함께 ErrorResponse 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     * @param message 작성한 메시지
     */
    private ErrorResponse(StatusCode errorCode, String message) {
        super(false, errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage(message));
    }

    /**
     * 반환할 ErrorCode와 함께 생성한 ErrorResponse 객체를 반환합니다.
     * @param errorCode 에러 상태 코드
     */
    public static ErrorResponse create(StatusCode errorCode){
        return new ErrorResponse(errorCode);
    }

    /**
     * 반환할 ErrorCode와 Exception 메시지와 함께 생성한 ErrorResponse 객체를 반환합니다.
     * @param errorCode 에러 상태 코드
     * @param e 발생한 예외
     */
    public static ErrorResponse create(StatusCode errorCode, Exception e){
        return new ErrorResponse(errorCode, e);
    }

    /**
     * 반환할 ErrorCode와 작성한 메시지와 함께 생성한 ErrorResponse 객체를 반환합니다.
     * @param errorCode 에러 상태 코드
     * @param message 작성한 메시지
     */
    public static ErrorResponse create(StatusCode errorCode, String message){
        return new ErrorResponse(errorCode, message);
    }
}
