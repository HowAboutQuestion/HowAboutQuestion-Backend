package com.howaboutquestion.backend.domain.auth.service;

import com.howaboutquestion.backend.domain.auth.dto.response.JwtTokenResponse;
import com.howaboutquestion.backend.domain.auth.dto.response.UserLoginResponse;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.global.service.RedisService;
import com.howaboutquestion.backend.global.util.JwtUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

/**
 * packageName    : com.howaboutquestion.backend.domain.auth.service<br>
 * fileName       : TokenService.java<br>
 * author         : cod0216 <br>
 * date           : 2025.07.13<br>
 * description    : JWT 토큰을 저장하거나 담는 Service 클래스입니다 <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초 생성 <br>
 */

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {


    private final RedisService redisService;
    private final JwtUtility jwtUtility;

    @Value("${jwt.refresh-token-validateTime}")
    private long refreshTokenValidity;

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_BEARER = "Bearer ";

    /**
     * 회원의 정보로 토큰을 생성 하고 저장하여 반환합니다.
     * @param user JWT 페이로더에 담을 회원 정보
     * @return 토큰 Response DTO
     */
    public JwtTokenResponse generateTokens(UserInfoResponse user){
        String tokenID = UUID.randomUUID().toString();

        String accessToken = jwtUtility.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUserType(), user.getProfile(), tokenID);
        String refreshToken = jwtUtility.createRefreshToken(user.getId(), user.getEmail(), user.getName(), user.getUserType(), user.getProfile(), tokenID);
        saveRefreshToken(user, refreshToken);
        return JwtTokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    /**
     * Redis에 RefreshToken을 저장합니다.
     * @param key Redis에 사용할 Key(UserId)
     * @param refreshToken Redis의 Key에 매핑 될 RefreshToken 값
     */

    private void saveRefreshToken(UserInfoResponse key, String refreshToken){
        redisService.saveRefreshToken(key.getId().toString(), refreshToken, Duration.ofMillis(refreshTokenValidity));
    }

}
