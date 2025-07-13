package com.howaboutquestion.backend.domain.usermeta.entity;

import com.howaboutquestion.backend.domain.guest.entity.GuestEntity;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * packageName    : com.howaboutquestion.backend.domain.usermeta.entity<br>
 * fileName       : UserMetaEntity.java<br>
 * author         : Eunchang<br>
 * date           : 2025-07-12<br>
 * description    : User Meta Entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.12          EunChang           최초생성<br>
 */

@Entity
@Getter
@Setter
@Table(name = "tb_user_meta")
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING, length = 5)
public abstract class UserMetaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
