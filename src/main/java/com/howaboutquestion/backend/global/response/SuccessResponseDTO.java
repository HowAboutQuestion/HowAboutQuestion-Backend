package com.howaboutquestion.backend.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.howaboutquestion.backend.global.common.ResponseDTO;
import com.howaboutquestion.backend.global.common.StatusCode;
import lombok.Getter;

/**
 * packageName    : com.howaboutquestion.backend.global.response<br>
 * fileName       : SuccessResponseDTO.java<br>
 * author         : eunchang <br>
 * date           : 2025-07-04<br>
 * description    : {@link ResponseDTO} 공통 성공 응답을 처리하는 SuccessResponseDTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang           최초생성<br>
 * 25.07.09          eunchang           클래스명 수정 및 메서드 분리<br>
 */

@Getter
@JsonPropertyOrder({"success", "httpCode", "httpStatus", "serverCode", "message", "data"})
public final class SuccessResponseDTO<T> extends ResponseDTO {
    private final T data;

    /**
     * 응답 데이터를 형식에 맞춰서 반환할 SuccessResponseDTO 클래스 생성자
     * @param data 응답 데이터
     */
    public SuccessResponseDTO(T data) {
        super(true, StatusCode.SUCCESS.getStatus().value(), StatusCode.SUCCESS.getStatus().name(), StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage());
        this.data = data;
    }

    /**
     * 응답 데이터를 커스텀한 메시지와 함께 반환할 Reponse 클래스 생성자
     * @param data 응답 데이터
     * @param message 커스텀 메시지
     */
    public SuccessResponseDTO(T data, String message) {
        super(true, StatusCode.SUCCESS.getStatus().value(), StatusCode.SUCCESS.name(), StatusCode.SUCCESS.getCode(), message);
        this.data = data;
    }

}
