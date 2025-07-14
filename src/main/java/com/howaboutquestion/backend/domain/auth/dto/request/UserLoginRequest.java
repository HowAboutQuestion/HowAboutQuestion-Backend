package com.howaboutquestion.backend.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserLoginRequest {
    @Email
    private String email;
    private String password;


}
