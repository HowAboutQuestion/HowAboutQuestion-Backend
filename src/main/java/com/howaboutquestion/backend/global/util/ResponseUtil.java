package com.howaboutquestion.backend.global.util;

import com.howaboutquestion.backend.global.common.ResponseDTO;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.FailureResponseDTO;
import com.howaboutquestion.backend.global.response.SuccessResponseDTO;
import org.springframework.http.ResponseEntity;

/**
 * packageName    : com.howaboutquestion.backend.global.util;<br>
 * fileName       : ResponseUtil.java<br>
 * author         : eunchang <br>
 * date           : 2025-07-09<br>
 * description    : ResponseDTO를 다루는 ResponseUtil 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.09          eunchang           최초생성<br>
 * 25.07.11          eunchang           create 함수를 사용해 생성자 호출<br
 */

public final class ResponseUtil {
    /**
     * 객체 생성을 방지합니다.
     */
    private ResponseUtil() {
        throw new UnsupportedOperationException("Utility 클래스는 생성할 수 없습니다.");
    }

    /**
     * 요청에 맞는 응답 데이터를 200 OK와 함께 반환합니다.
     * @param data 응답 데이터
     * @return 데이터를 포함한 200 OK 응답 객체
     * @param <T> 응답 데이터 타입
     */
    public static <T> ResponseEntity<SuccessResponseDTO<T>> success (T data){
        return ResponseEntity.ok(SuccessResponseDTO.create(data));
    }

    /**
     * 요청에 맞는 응답 데이터와 메시지를 200 OK와 함께 반환합니다.
     * @param data 응답 데이터
     * @param message 응답과 함께 반환할 메시지
     * @return 데이터를 포함한 200 OK 응답 객체
     * @param <T> 응답 데이터 타입
     */
    public static <T> ResponseEntity<SuccessResponseDTO<T>> success (T data, String message){
        return ResponseEntity.ok(SuccessResponseDTO.create(data, message));
    }

    /**
     * 지정한 StatusCode로 실패 응답을 반환합니다.
     * @param errorCode 반환할 에러 정보
     * @return 실패 응답 객체
     */
    public static ResponseEntity<FailureResponseDTO> failure(StatusCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(FailureResponseDTO.create(errorCode));
    }

    /**
     * 지정한 StatusCode와 메시지로 실패 응답을 반환합니다.
     * @param errorCode 반환할 에러 정보
     * @param message 사용자 메시지
     * @return 실패 응답 객체
     */
    public static ResponseEntity<FailureResponseDTO> failure(StatusCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getStatus()).body(FailureResponseDTO.create(errorCode, message));
    }
}
