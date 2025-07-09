package com.howaboutquestion.backend.global;

import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.FailureResponseDTO;
import com.howaboutquestion.backend.global.response.SuccessResponseDTO;
import com.howaboutquestion.backend.global.util.ResponseUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * packageName    : com.howaboutquestion.backend.global<br>
 * fileName       : SuccessResponseDTOTest.java<br>
 * author         : eunchang<br>
 * date           : 2025-07-04<br>
 * description    : Response와 Error를 테스트 하는 테스트 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.04          eunchang           최초생성<br>
 * 25.07.09          eunchang           변경된 ResponseDTO 반영<br>
 */

public class SuccessResponseDTOTest {
    @DisplayName("데이터가 포함된 200 성공 응답 테스트")
    @Test
    void SuccessResponseTest(){

        // given
        Map<String, String> data = new HashMap<>();
        data.put("name","권해림");
        data.put("age","26");

        // when
        SuccessResponseDTO originData = new SuccessResponseDTO(data);

        // then
        assertThat(originData.isSuccess()).isTrue();
        assertThat(originData.getHttpCode()).isEqualTo(200);
        assertThat(originData.getHttpStatus()).isEqualTo("OK");
        assertThat(originData.getServerCode()).isEqualTo("SUCCESS-200");
        assertThat(originData.getMessage()).isEqualTo("요청이 완료되었습니다.");
        assertThat(originData.getData()).isEqualTo(data);
    }

    @DisplayName("빈 데이터를 반환하는 200 성공 응답 테스트")
    @Test
    void SuccessNotDataResponseTest(){

        // given
        Map<String, String> data = new HashMap<>();
        data.put("name","권해림");
        data.put("age","26");

        // when
        SuccessResponseDTO originData = new SuccessResponseDTO(null);


        // then
        assertThat(originData.isSuccess()).isTrue();
        assertThat(originData.getHttpCode()).isEqualTo(200);
        assertThat(originData.getHttpStatus()).isEqualTo("OK");
        assertThat(originData.getServerCode()).isEqualTo("SUCCESS-200");
        assertThat(originData.getMessage()).isEqualTo("요청이 완료되었습니다.");
        assertThat(originData.getData()).isEqualTo(null);
    }

    @DisplayName("토큰 에러를 반환하는 실패 응답 테스트")
    @Test
    void ErrorResponseTest(){

        // given

        // when
        FailureResponseDTO failureResponseDTO = new FailureResponseDTO(StatusCode.INVALID_TOKEN);

        // then
        assertThat(failureResponseDTO.isSuccess()).isFalse();
        assertThat(failureResponseDTO.getHttpCode()).isEqualTo(401);
        assertThat(failureResponseDTO.getHttpStatus()).isEqualTo("UNAUTHORIZED");
        assertThat(failureResponseDTO.getServerCode()).isEqualTo("TOKEN-001");
        assertThat(failureResponseDTO.getMessage()).isEqualTo("유효하지 않은 토큰입니다.");
    }
}
