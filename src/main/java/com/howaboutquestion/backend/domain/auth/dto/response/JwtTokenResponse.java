package com.howaboutquestion.backend.domain.auth.dto.response;

import lombok.*;

/**
 * packageName    : com.howaboutquestion.backend.domain.auth.dto.response<br>
 * fileName       : JwtTokenResponse.java<br>
 * author         : eunchang <br>
 * date           : 2025.07.13<br>
 * description    : 토큰 정보를 담는 Response DTO 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          eunchang           최초 생성 <br>
 */

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenResponse {
    private String accessToken;
    private String refreshToken;
}
