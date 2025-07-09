package com.howaboutquestion.backend.global.util;

import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.FailureResponseDTO;
import com.howaboutquestion.backend.global.response.SuccessResponseDTO;
import org.springframework.http.ResponseEntity;

public final class ResponseUtil {


    /**
     * 요청에 맞는 응답 데이터를 200 OK와 함께 반환합니다.
     * @param data 응답 데이터
     * @return 데이터를 포함한 200 OK 응답 객체
     * @param <T> 응답 데이터 타입
     */
    public static <T> ResponseEntity<SuccessResponseDTO<T>> success (T data){
        return ResponseEntity.ok(new SuccessResponseDTO<>(data));
    }

    /**
     * 요청에 맞는 응답 데이터와 메시지를 200 OK와 함께 반환합니다.
     * @param data 응답 데이터
     * @param message 응답과 함께 반환할 메시지
     * @return 데이터를 포함한 200 OK 응답 객체
     * @param <T> 응답 데이터 타입
     */
    public static <T> ResponseEntity<SuccessResponseDTO<T>> success (T data, String message){
        return ResponseEntity.ok(new SuccessResponseDTO<>(data, message));
    }

    /**
     * 지정한 StatusCode로 실패 응답을 반환합니다.
     * @param errorCode 반환할 에러 정보
     * @return 실패 응답 객체
     */
    public static ResponseEntity<FailureResponseDTO> failure(StatusCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(new FailureResponseDTO(errorCode));
    }

    /**
     * 지정한 StatusCode와 메시지로 실패 응답을 반환합니다.
     * @param errorCode 반환할 에러 정보
     * @param message 사용자 메시지
     * @return 실패 응답 객체
     */
    public static ResponseEntity<FailureResponseDTO> failure(StatusCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getStatus()).body(new FailureResponseDTO(errorCode, message));
    }
}
