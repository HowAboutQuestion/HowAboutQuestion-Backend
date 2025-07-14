package com.howaboutquestion.backend.domain.user.dto.resopnse;

import com.howaboutquestion.backend.domain.usermeta.entity.UserType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponse {
    private Integer id;
    private LocalDateTime createdAt;
    private UserType userType;
    private String email;
    private String name;
    private String profile;
}
