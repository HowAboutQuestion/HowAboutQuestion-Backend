package com.howaboutquestion.backend.domain.auth.controller;

import com.howaboutquestion.backend.domain.auth.dto.request.RefreshTokenRequest;
import com.howaboutquestion.backend.domain.auth.dto.request.UserLoginRequest;
import com.howaboutquestion.backend.domain.auth.service.AuthService;
import com.howaboutquestion.backend.domain.auth.dto.request.UserRegisterRequest;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.howaboutquestion.backend.domain.auth.controller<br>
 * fileName       : AuthController.java<br>
 * author         : cod0216 <br>
 * date           : 2025.07.13<br>
 * description    : 인증 요청을 처리 해주는 Controller 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초 생성 <br>
 * 26.05.06          cod0216           로그아웃 및 토큰 재발급 API 추가<br>
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class AuthController {

    private final AuthService authService;

    /**
     * USER의 회원가입을 담당하는 API
     * @param request 회원 가입 요청
     * @return 요청에 대한 결과를 반환합니다.
     */

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterRequest request){
        return ResponseUtility.success(authService.basicRegister(request), "회원가입이 완료되었습니다.");
    }

    /**
     * USER의 로그인을 담당하는 API
     * @param request 로그인 요청
     * @return 회원 정보와 토큰 정보를 반환 합니다.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest request){
        return ResponseUtility.success(authService.basicLogin(request));
    }

    /**
     * USER의 로그아웃을 담당하는 API
     * @param userDetail 인증된 사용자 정보
     * @return 로그아웃 결과를 반환합니다.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetail userDetail) {
        authService.logout(Long.parseLong(userDetail.getUserId()));
        return ResponseUtility.success(null, "로그아웃이 완료되었습니다.");
    }

    /**
     * Refresh Token으로 토큰 재발급을 담당하는 API
     * @param request Refresh Token 요청
     * @return 재발급된 토큰 정보를 반환합니다.
     */
    @PostMapping("/tokens/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseUtility.success(authService.refresh(request));
    }
}
