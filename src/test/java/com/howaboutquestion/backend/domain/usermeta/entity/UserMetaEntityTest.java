package com.howaboutquestion.backend.domain.usermeta.entity;

import com.howaboutquestion.backend.domain.guest.entity.GuestEntity;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMetaEntityTest {

    @DisplayName("회원 엔티티는 USER 타입을 반환한다")
    @Test
    void userEntityReturnsUserType() {
        UserEntity userEntity = UserEntity.builder()
                .email("user@example.com")
                .name("tester")
                .password("encoded-password")
                .build();

        assertThat(userEntity.getUserType()).isEqualTo(UserType.USER);
    }

    @DisplayName("게스트 엔티티는 GUEST 타입을 반환한다")
    @Test
    void guestEntityReturnsGuestType() {
        GuestEntity guestEntity = GuestEntity.builder()
                .uuid("guest-uuid")
                .build();

        assertThat(guestEntity.getUserType()).isEqualTo(UserType.GUEST);
    }
}
