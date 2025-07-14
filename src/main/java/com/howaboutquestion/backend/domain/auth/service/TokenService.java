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

    public JwtTokenResponse generateTokens(UserInfoResponse user){
        String tokenID = UUID.randomUUID().toString();

        String accessToken = jwtUtility.createAccessToken(user.getId(), user.getName(), user.getUserType(), tokenID);
        String refreshToken = jwtUtility.createRefreshToken(user.getId(), user.getName(), user.getUserType(), tokenID);
        saveRefreshToken(user, refreshToken);
        return JwtTokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    private void saveRefreshToken(UserInfoResponse key, String refreshToken){
        redisService.setValue(key.getId().toString(), refreshToken, Duration.ofMillis(refreshTokenValidity));
    }

}
