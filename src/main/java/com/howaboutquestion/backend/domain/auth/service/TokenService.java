package com.howaboutquestion.backend.domain.auth.service;

import com.howaboutquestion.backend.domain.auth.dto.response.JwtTokenResponse;
import com.howaboutquestion.backend.domain.user.dto.mapper.UserMapper;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.repository.UserRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import com.howaboutquestion.backend.global.util.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * 26.04.28          cod0216           Refresh Token을 User 컬럼에 저장하도록 수정<br>
 * 26.05.06          eunchang          로그아웃 및 토큰 재발급 로직 추가<br>
 */

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {


    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;
    private final UserMapper userMapper;

    /**
     * 회원의 정보로 토큰을 생성 하고 저장하여 반환합니다.
     * @param user JWT 페이로더에 담을 회원 정보
     * @return 토큰 Response DTO
     */
    public JwtTokenResponse generateTokens(UserInfoResponse user){
        String tokenID = UUID.randomUUID().toString();

        String accessToken = jwtUtility.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUserType(), user.getProfile(), tokenID);
        String refreshToken = jwtUtility.createRefreshToken(user.getId(), user.getEmail(), user.getName(), user.getUserType(), user.getProfile(), tokenID);
        saveRefreshToken(user.getId(), refreshToken);
        return JwtTokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    /**
     * DB에 RefreshToken을 저장합니다.
     * @param userId 사용자 Id
     * @param refreshToken 저장할 RefreshToken 값
     */
    private void saveRefreshToken(Long userId, String refreshToken){
        UserEntity userEntity = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_USER));
        userEntity.updateRefreshToken(refreshToken);
        userRepository.save(userEntity);
    }

    /**
     * DB에 저장된 RefreshToken을 제거합니다.
     * @param userId 사용자 Id
     */
    public void clearRefreshToken(Long userId) {
        UserEntity userEntity = findUserById(userId);
        userEntity.clearRefreshToken();
        userRepository.save(userEntity);
    }

    /**
     * Refresh Token을 검증하고 새 토큰을 발급합니다.
     * @param refreshToken Refresh Token 값
     * @return 새로 발급된 토큰 정보
     */
    public JwtTokenResponse reissueTokens(String refreshToken) {
        jwtUtility.validateToken(refreshToken);

        Long userId = jwtUtility.getUserId(refreshToken);
        UserEntity userEntity = findUserById(userId);

        if (userEntity.getRefreshToken() == null || !userEntity.getRefreshToken().equals(refreshToken)) {
            throw new CustomException(StatusCode.INVALID_TOKEN);
        }

        return generateTokens(userMapper.mapToUserInfoResponse(userEntity));
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_USER));
    }

}
