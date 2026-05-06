package com.howaboutquestion.backend.domain.auth.dto.request;

import com.howaboutquestion.backend.domain.usermeta.entity.UserType;
import lombok.*;
/**
 * packageName    : com.howaboutquestion.backend.domain.auth.dto.request<br>
 * fileName       : TokenUserInfo.java<br>
 * author         : eunchang <br>
 * date           : 2025.07.13<br>
 * description    : 토큰에서 유저 정보를 담는 DTO 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.17          eunchang           최초 생성 <br>
 * 25.07.03          eunchang           ID 타입 Long 변경 <br>
 */

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenUserInfo {
    private Long id;
    private UserType userType;
    private String email;
    private String name;
    private String profile;
}
