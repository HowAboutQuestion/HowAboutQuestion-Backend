package com.howaboutquestion.backend.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.howaboutquestion.backend.domain.auth.dto.request<br>
 * fileName       : RefreshTokenRequest.java<br>
 * author         : cod0216<br>
 * date           : 2026.05.06<br>
 * description    : Refresh Token 재발급 요청 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          cod0216           최초 생성<br>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;
}
