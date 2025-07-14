package com.howaboutquestion.backend.domain.auth.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenResponse {
    private String accessToken;
    private String refreshToken;
}
