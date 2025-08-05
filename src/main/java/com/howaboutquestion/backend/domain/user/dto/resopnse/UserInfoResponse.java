package com.howaboutquestion.backend.domain.user.dto.resopnse;

import com.howaboutquestion.backend.domain.usermeta.entity.UserType;
import lombok.*;

import java.time.LocalDateTime;
/**
 * packageName    : com.howaboutquestion.backend.domain.user.dto.resopnse<br>
 * fileName       : UserInfoResponse.java<br>
 * author         : cod0216 <br>
 * date           : 2025.07.13<br>
 * description    : 로그인 시 사용자 정보를 담는 Response DTO 클래스입니다<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초 생성 <br>
 */

@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponse {
    private Long id;
    private LocalDateTime createdAt;
    private UserType userType;
    private String email;
    private String name;
    private String profile;
}
