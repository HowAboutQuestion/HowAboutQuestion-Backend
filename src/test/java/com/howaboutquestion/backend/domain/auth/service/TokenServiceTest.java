package com.howaboutquestion.backend.domain.auth.service;

import com.howaboutquestion.backend.domain.auth.dto.response.JwtTokenResponse;
import com.howaboutquestion.backend.domain.user.dto.mapper.UserMapper;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.repository.UserRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import com.howaboutquestion.backend.domain.usermeta.entity.UserType;
import com.howaboutquestion.backend.global.util.JwtUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtility jwtUtility;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private TokenService tokenService;

    @DisplayName("로그인 토큰 생성 시 User 엔티티의 RefreshToken을 갱신한다")
    @Test
    void generateTokensUpdatesUserRefreshToken() {
        UserInfoResponse user = createUserInfoResponse();
        UserEntity userEntity = createUserEntity();

        given(jwtUtility.createAccessToken(any(), any(), any(), any(), any(), any())).willReturn("access-token");
        given(jwtUtility.createRefreshToken(any(), any(), any(), any(), any(), any())).willReturn("refresh-token");
        given(userRepository.findById(user.getId().intValue())).willReturn(Optional.of(userEntity));

        JwtTokenResponse response = tokenService.generateTokens(user);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(userEntity.getRefreshToken()).isEqualTo("refresh-token");
        verify(userRepository).save(userEntity);
    }

    @DisplayName("동일 사용자가 다시 로그인하면 기존 RefreshToken 값을 덮어쓴다")
    @Test
    void generateTokensOverridesExistingRefreshToken() {
        UserInfoResponse user = createUserInfoResponse();
        UserEntity userEntity = UserEntity.builder()
                .email("user@example.com")
                .name("tester")
                .password("encoded-password")
                .profile("profile.png")
                .refreshToken("old-refresh-token")
                .build();

        given(jwtUtility.createAccessToken(any(), any(), any(), any(), any(), any())).willReturn("access-token");
        given(jwtUtility.createRefreshToken(any(), any(), any(), any(), any(), any())).willReturn("refresh-token");
        given(userRepository.findById(user.getId().intValue())).willReturn(Optional.of(userEntity));

        tokenService.generateTokens(user);

        assertThat(userEntity.getRefreshToken()).isEqualTo("refresh-token");
        verify(userRepository).save(userEntity);
    }

    @DisplayName("로그아웃 시 저장된 RefreshToken을 제거한다")
    @Test
    void clearRefreshTokenRemovesStoredRefreshToken() {
        UserEntity userEntity = UserEntity.builder()
                .email("user@example.com")
                .name("tester")
                .password("encoded-password")
                .profile("profile.png")
                .refreshToken("refresh-token")
                .build();

        given(userRepository.findById(1)).willReturn(Optional.of(userEntity));

        tokenService.clearRefreshToken(1L);

        assertThat(userEntity.getRefreshToken()).isNull();
        verify(userRepository).save(userEntity);
    }

    @DisplayName("유효한 RefreshToken이면 새 AccessToken과 RefreshToken을 재발급한다")
    @Test
    void reissueTokensReturnsNewTokensWhenRefreshTokenMatchesStoredValue() {
        UserEntity userEntity = createUserEntity();
        userEntity.updateRefreshToken("stored-refresh-token");
        UserInfoResponse userInfoResponse = createUserInfoResponse();

        given(userRepository.findById(1)).willReturn(Optional.of(userEntity));
        given(jwtUtility.validateToken("stored-refresh-token")).willReturn(true);
        given(jwtUtility.getUserId("stored-refresh-token")).willReturn(1L);
        given(userMapper.mapToUserInfoResponse(userEntity)).willReturn(userInfoResponse);
        given(jwtUtility.createAccessToken(any(), any(), any(), any(), any(), any())).willReturn("new-access-token");
        given(jwtUtility.createRefreshToken(any(), any(), any(), any(), any(), any())).willReturn("new-refresh-token");

        JwtTokenResponse response = tokenService.reissueTokens("stored-refresh-token");

        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");
        assertThat(userEntity.getRefreshToken()).isEqualTo("new-refresh-token");
        verify(userRepository).save(userEntity);
    }

    @DisplayName("저장된 RefreshToken과 다르면 재발급을 거부한다")
    @Test
    void reissueTokensThrowsWhenRefreshTokenDoesNotMatchStoredValue() {
        UserEntity userEntity = createUserEntity();
        userEntity.updateRefreshToken("stored-refresh-token");

        given(userRepository.findById(1)).willReturn(Optional.of(userEntity));
        given(jwtUtility.validateToken("different-refresh-token")).willReturn(true);
        given(jwtUtility.getUserId("different-refresh-token")).willReturn(1L);

        assertThatThrownBy(() -> tokenService.reissueTokens("different-refresh-token"))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.INVALID_TOKEN);
    }

    private UserInfoResponse createUserInfoResponse() {
        return UserInfoResponse.builder()
                .id(1L)
                .email("user@example.com")
                .name("tester")
                .profile("profile.png")
                .userType(UserType.USER)
                .createdAt(LocalDateTime.of(2026, 4, 1, 0, 0))
                .build();
    }

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .email("user@example.com")
                .name("tester")
                .password("encoded-password")
                .profile("profile.png")
                .build();
    }
}
