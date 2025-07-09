package com.howaboutquestion.backend.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.howaboutquestion.backend.global.common<br>
 * fileName       : ResponseDTO.java<br>
 * author         : eunchang <br>
 * date           : 2025-07-04<br>
 * description    : 응답에 필요한 공통 매개 변수를 지정하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang           최초생성<br>
 * 25.07.09          eunchang           클래스명 수정 및 추상 클래스 전환<br>
 * <br>
 */
@Getter
@AllArgsConstructor
public abstract class ResponseDTO {
    private boolean success;
    private int httpCode;
    private String httpStatus;
    private String serverCode;
    private String message;
}


