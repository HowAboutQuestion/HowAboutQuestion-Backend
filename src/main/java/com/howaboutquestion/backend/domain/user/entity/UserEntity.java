package com.howaboutquestion.backend.domain.user.entity;

import com.howaboutquestion.backend.domain.usermeta.entity.UserMetaEntity;
import jakarta.persistence.*;
import lombok.*;
/**
 * packageName    : com.howaboutquestion.backend.domain.user.entity<br>
 * fileName       : UserEntity.java<br>
 * author         : cod0216 <br>
 * date           : 2025-07-13<br>
 * description    : User entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초생성<br>
 * 26.04.28          cod0216           Refresh Token 컬럼 및 갱신 메서드 추가<br>
 */
@Entity
@Getter
@Setter
@Builder
@Table(name = "tb_user")
@DiscriminatorValue("USER")
@PrimaryKeyJoinColumn(name = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends UserMetaEntity {
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    private String profile;

    @Column(name = "refresh_token", length = 512)
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void clearRefreshToken() {
        this.refreshToken = null;
    }
}
