package com.howaboutquestion.backend.domain.user.entity;

import com.howaboutquestion.backend.domain.usermeta.entity.UserMetaEntity;
import jakarta.persistence.*;
import lombok.*;

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
}
