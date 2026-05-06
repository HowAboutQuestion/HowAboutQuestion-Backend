package com.howaboutquestion.backend.domain.auth.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * packageName    : com.howaboutquestion.backend.domain.auth.dto.response<br>
 * fileName       : UserRegisterResponse.java<br>
 * author         : eunchang <br>
 * date           : 2025.07.13<br>
 * description    : USER의 회원가입을 위한 Response DTO 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          eunchang           최초 생성 <br>
 */
@Data
@AllArgsConstructor
public class UserRegisterResponse {

    private Long id;
    private LocalDateTime createdAt;
    private String email;
    private String name;
    private String profile;



}
