package com.howaboutquestion.backend.domain.auth.dto.response;

import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import lombok.*;

/**
 * packageName    : com.howaboutquestion.backend.domain.auth.dto.response<br>
 * fileName       : UserLoginResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2025.07.13<br>
 * description    : USER의 로그인을 위한 Response DTO 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초 생성 <br>
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginResponse {
    private UserInfoResponse userInfoResponse;
    private JwtTokenResponse jwtTokenResponse;
}
