package com.howaboutquestion.backend.domain.auth.service;

import com.howaboutquestion.backend.domain.auth.dto.response.JwtTokenResponse;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtility jwtUtility;

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
