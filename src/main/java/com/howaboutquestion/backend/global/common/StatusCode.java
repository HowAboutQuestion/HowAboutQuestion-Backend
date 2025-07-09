package com.howaboutquestion.backend.global.common;

import com.howaboutquestion.backend.global.error.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * packageName    : com.howaboutquestion.backend.global.common<br>
 * fileName       : StatusCode.java<br>
 * author         : eunchang <br>
 * date           : 2025-07-04<br>
 * description    : 응답에 사용될 상태 코드를 정의하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang           최초생성<br>
 * 25.07.09          eunchang           getMessage 메서드 삭제<br>
 */

@Getter
@RequiredArgsConstructor
public enum StatusCode {
    SUCCESS(HttpStatus.OK, "SUCCESS-200", "요청이 완료되었습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "ERROR-400", "잘못된 요청입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR-404", "페이지가 존재하지 않습니다."),
    NO_USER_PERMISSION(HttpStatus.FORBIDDEN, "ERROR-403", "페이지 대한 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR-500", "서버 내부 오류가 발생했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN-001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN-002", "만료된 토큰입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    /**
     * HttpStatus에 매핑되는 StatusCode를 반환합니다.
     * @param httpStatus HttpStatus 객체
     * @return 매핑된 StatusCode
     */
    public static StatusCode valueOf(HttpStatus httpStatus){
        if(Objects.isNull(httpStatus)) throw new CustomException("상태를 가져오는 중 문제가 발생하였습니다.");

        for(StatusCode code : values()){
            if(code.getStatus() == httpStatus) return code;
        }

        if(httpStatus.is4xxClientError()) return StatusCode.INVALID_PARAMETER;
        else if(httpStatus.is5xxServerError()) return StatusCode.INTERNAL_SERVER_ERROR;
        else return StatusCode.SUCCESS;
    }

}
