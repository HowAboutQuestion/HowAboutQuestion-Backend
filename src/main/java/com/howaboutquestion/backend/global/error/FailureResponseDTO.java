package com.howaboutquestion.backend.global.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.howaboutquestion.backend.global.common.ResponseDTO;
import com.howaboutquestion.backend.global.common.StatusCode;
import lombok.*;

/**
 * packageName    : com.howaboutquestion.backend.global.error<br>
 * fileName       : FailureResponseDTO.java<br>
 * author         : eunchang <br>
 * date           : 2025-07-04<br>
 * description    : {@link ResponseDTO}를 상속받은 FailureResponseDTO 를 반환하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang          최초생성<br>
 * 25.07.09          eunchang          클래스명 수정<br>
 */

@Getter
@JsonPropertyOrder({"success", "httpCode", "httpStatus", "serverCode", "message"})
public final class FailureResponseDTO extends ResponseDTO {

    /**
     * 반환할 ErrorCode와 함께 FailureResponseDTO 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     */
    public FailureResponseDTO(StatusCode errorCode) {
        super(false, errorCode.getStatus().value(), errorCode.getStatus().name(), errorCode.getCode(), errorCode.getMessage());
    }


    /**
     * 반환할 ErrorCode와 작성한 메시지와 함께 FailureResponseDTO 객체를 생성합니다.
     * @param errorCode 에러 상태 코드
     * @param message 작성한 메시지
     */
    public FailureResponseDTO(StatusCode errorCode, String message) {
        super(false,  errorCode.getStatus().value(), errorCode.getStatus().name(), errorCode.getCode(), message);
    }
}
