package com.howaboutquestion.backend.domain.auth.service;

import com.howaboutquestion.backend.domain.auth.dto.response.JwtTokenResponse;
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
 */

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {


    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;

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

}
