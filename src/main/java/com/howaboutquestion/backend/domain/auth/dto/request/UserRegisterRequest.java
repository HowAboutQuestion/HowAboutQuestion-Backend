package com.howaboutquestion.backend.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

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
    private String profile;

}
