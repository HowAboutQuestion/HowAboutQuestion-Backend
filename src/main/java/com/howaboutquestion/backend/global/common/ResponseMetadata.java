package com.howaboutquestion.backend.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.howaboutquestion.backend.global.common<br>
 * fileName       : ResponseMetadata.java<br>
 * author         : eunchang <br>
 * date           : 2025-06-19<br>
 * description    : 응답에 필요한 공통 매개 변수를 지정하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang           최초생성<br>
 * <br>
 */
@Getter
@RequiredArgsConstructor
public class ResponseMetadata {
    private final boolean success;
    private final HttpStatus status;
    private final String code;
    private final String message;
}


