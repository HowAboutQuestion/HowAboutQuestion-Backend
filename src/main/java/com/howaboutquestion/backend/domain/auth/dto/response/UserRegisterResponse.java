package com.howaboutquestion.backend.domain.auth.dto.response;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class UserRegisterResponse {

    private Integer id;
    private LocalDateTime createdAt;
    private String email;
    private String name;
    private String profile;



}
