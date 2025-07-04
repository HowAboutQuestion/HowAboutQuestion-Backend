package com.howaboutquestion.backend.global;

import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.ErrorResponse;
import com.howaboutquestion.backend.global.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * packageName    : com.howaboutquestion.backend.global<br>
 * fileName       : ResponseTest.java<br>
 * author         : eunchang<br>
 * date           : 2025-07-04<br>
 * description    : Response와 Error를 테스트 하는 테스트 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang           최초생성<br>
 */

public class ResponseTest {
    @DisplayName("데이터가 포함된 200 성공 응답 테스트")
    @Test
    void SuccessResponseTest(){

        // given
        Map<String, String> data = new HashMap<>();
        data.put("name","권해림");
        data.put("age","26");

        // when
        Response response = Response.success(data);

        // then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus().value()).isEqualTo(200);
        assertThat(response.getCode()).isEqualTo("SUCCESS-200");
        assertThat(response.getMessage()).isEqualTo("요청이 완료되었습니다.");
        assertThat(response.getData()).isEqualTo(data);
    }

    @DisplayName("빈 데이터를 반환하는 200 성공 응답 테스트")
    @Test
    void SuccessNotDataResponseTest(){

        // given
        Map<String, String> data = new HashMap<>();
        data.put("name","권해림");
        data.put("age","26");

        // when
        Response response = Response.empty();

        // then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus().value()).isEqualTo(200);
        assertThat(response.getCode()).isEqualTo("SUCCESS-200");
        assertThat(response.getMessage()).isEqualTo("요청이 완료되었습니다.");
        assertThat(response.getData()).isEqualTo(null);
    }

    @DisplayName("토큰 에러를 반환하는 실패 응답 테스트")
    @Test
    void ErrorResponseTest(){

        // given

        // when
        ErrorResponse errorResponse = ErrorResponse.create(StatusCode.INVALID_TOKEN);

        // then
        assertThat(errorResponse.isSuccess()).isFalse();
        assertThat(errorResponse.getStatus().value()).isEqualTo(401);
        assertThat(errorResponse.getCode()).isEqualTo("TOKEN-001");
        assertThat(errorResponse.getMessage()).isEqualTo("유효하지 않은 토큰입니다.");
    }
}
