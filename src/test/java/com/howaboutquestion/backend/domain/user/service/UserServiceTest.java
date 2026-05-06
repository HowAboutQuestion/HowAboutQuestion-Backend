package com.howaboutquestion.backend.domain.user.service;

import com.howaboutquestion.backend.domain.user.dto.mapper.UserMapper;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.repository.UserRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @DisplayName("현재 로그인한 사용자 정보 조회에 성공한다")
    @Test
    void getCurrentUserInfoReturnsMappedResponse() {
        UserEntity userEntity = UserEntity.builder()
                .email("user@example.com")
                .name("tester")
                .password("encoded-password")
                .profile("profile.png")
                .build();
        UserInfoResponse response = UserInfoResponse.builder()
                .id(1L)
                .email("user@example.com")
                .name("tester")
                .profile("profile.png")
                .build();

        given(userRepository.findById(1)).willReturn(Optional.of(userEntity));
        given(userMapper.mapToUserInfoResponse(userEntity)).willReturn(response);

        UserInfoResponse result = userService.getCurrentUserInfo(1L);

        assertThat(result).isSameAs(response);
    }

    @DisplayName("현재 로그인한 사용자가 없으면 예외를 반환한다")
    @Test
    void getCurrentUserInfoThrowsWhenUserDoesNotExist() {
        given(userRepository.findById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getCurrentUserInfo(1L))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.NOT_FOUND_USER);
    }
}
