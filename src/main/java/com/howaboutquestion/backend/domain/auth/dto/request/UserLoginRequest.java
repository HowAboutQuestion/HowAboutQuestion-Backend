package com.howaboutquestion.backend.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.ToString;
/**
 * packageName    : com.howaboutquestion.backend.domain.auth.dto.request<br>
 * fileName       : UserLoginRequest.java<br>
 * author         : cod0216 <br>
 * date           : 2025.07.13<br>
 * description    : 로그인을 요청하는 DTO 입니다 <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초 생성 <br>
 */
@Getter
public class UserLoginRequest {
    @Email
    private String email;
    private String password;

    @Override
    public String toString() {
        return "UserLoginRequest(" +
                "email='" + email + '\'' +
                ", password='****" + '\'' +
                ')';
    }
}
