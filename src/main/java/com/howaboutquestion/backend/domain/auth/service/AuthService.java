package com.howaboutquestion.backend.domain.auth.service;

import com.howaboutquestion.backend.domain.auth.dto.request.UserRegisterRequest;
import com.howaboutquestion.backend.domain.auth.dto.response.UserRegisterResponse;
import com.howaboutquestion.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    public UserRegisterResponse basicRegister(UserRegisterRequest request){
        return userService.tryRegisterUser(request);
    }


}
