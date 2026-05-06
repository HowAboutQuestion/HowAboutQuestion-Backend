package com.howaboutquestion.backend.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

/**
 * packageName    : com.howaboutquestion.backend.domain.auth.dto.request<br>
 * fileName       : UserRegisterRequest.java<br>
 * author         : eunchang <br>
 * date           : 2025.07.13<br>
 * description    : 회원가입을 요청하는 DTO 입니다 <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          eunchang           최초 생성 <br>
 */

@Getter
@ToString
public class UserRegisterRequest {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private String name;

}
