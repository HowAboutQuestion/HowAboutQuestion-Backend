package com.howaboutquestion.backend.domain.guest.entity;

import com.howaboutquestion.backend.domain.usermeta.entity.UserMetaEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.howaboutquestion.backend.domain.guest.entity<br>
 * fileName       : GuestEntity.java<br>
 * author         : cod0216 <br>
 * date           : 2025-07-13<br>
 * description    : Guest entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초생성<br>
 */

@Entity
@Getter
@Setter
@Builder
@Table(name = "tb_guest")
@DiscriminatorValue("GUEST")
@PrimaryKeyJoinColumn(name = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestEntity extends UserMetaEntity {
    @Column
    private String uuid;
}
