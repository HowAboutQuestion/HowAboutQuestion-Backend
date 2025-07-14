package com.howaboutquestion.backend.domain.auth.dto.response;

import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginResponse {
    private UserInfoResponse userInfoResponse;
    private JwtTokenResponse jwtTokenResponse;
}
