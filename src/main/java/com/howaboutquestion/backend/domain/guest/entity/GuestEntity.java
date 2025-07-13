package com.howaboutquestion.backend.domain.guest.entity;

import com.howaboutquestion.backend.domain.usermeta.entity.UserMetaEntity;
import jakarta.persistence.*;
import lombok.*;

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
