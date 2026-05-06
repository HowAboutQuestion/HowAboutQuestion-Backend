package com.howaboutquestion.backend.domain.auth.service;

import com.howaboutquestion.backend.domain.auth.dto.request.RefreshTokenRequest;
import com.howaboutquestion.backend.domain.auth.dto.request.UserLoginRequest;
import com.howaboutquestion.backend.domain.auth.dto.request.UserRegisterRequest;
import com.howaboutquestion.backend.domain.auth.dto.response.JwtTokenResponse;
import com.howaboutquestion.backend.domain.auth.dto.response.UserLoginResponse;
import com.howaboutquestion.backend.domain.auth.dto.response.UserRegisterResponse;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.howaboutquestion.backend.domain.auth.service<br>
 * fileName       : AuthService.java<br>
 * author         : cod0216 <br>
 * date           : 2025.07.13<br>
 * description    : 인증 요청을 처리 해주는 Service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초 생성 <br>
 * 26.05.06          eunchang          로그아웃 및 토큰 재발급 서비스 추가<br>
 */

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;


    /**
     * USER의 회원가입을 시도 합니다.
     * @param request 회원 가입 요청 정보
     * @return 회원 정보
     */
    public UserRegisterResponse basicRegister(UserRegisterRequest request){
        return userService.tryRegisterUser(request);
    }

    /**
     * 로그인을 시도합니다.
     * @param request 로그인 정보
     * @return 회원 정보 및 토큰 정보
     */

    public UserLoginResponse basicLogin(UserLoginRequest request){
        UserInfoResponse userInfo = userService.tryUserLogin(request);
        JwtTokenResponse tokens = tokenService.generateTokens(userInfo);
        UserLoginResponse response = UserLoginResponse.builder().userInfoResponse(userInfo).jwtTokenResponse(tokens).build();

        return response;
    }

    /**
     * 로그아웃을 시도합니다.
     * @param userId 로그인한 사용자 Id
     */
    public void logout(Long userId) {
        tokenService.clearRefreshToken(userId);
    }

    /**
     * Refresh Token으로 토큰 재발급을 시도합니다.
     * @param request Refresh Token 요청 정보
     * @return 재발급된 토큰 정보
     */
    public JwtTokenResponse refresh(RefreshTokenRequest request) {
        return tokenService.reissueTokens(request.getRefreshToken());
    }

}
