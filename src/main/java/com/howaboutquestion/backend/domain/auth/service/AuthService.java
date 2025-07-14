package com.howaboutquestion.backend.domain.auth.service;

import com.howaboutquestion.backend.domain.auth.dto.request.UserLoginRequest;
import com.howaboutquestion.backend.domain.auth.dto.request.UserRegisterRequest;
import com.howaboutquestion.backend.domain.auth.dto.response.JwtTokenResponse;
import com.howaboutquestion.backend.domain.auth.dto.response.UserLoginResponse;
import com.howaboutquestion.backend.domain.auth.dto.response.UserRegisterResponse;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;

    public UserRegisterResponse basicRegister(UserRegisterRequest request){
        return userService.tryRegisterUser(request);
    }

    public UserLoginResponse basicLogin(UserLoginRequest request){
        UserInfoResponse userInfo = userService.tryUserLogin(request);
        JwtTokenResponse tokens = tokenService.generateTokens(userInfo);

        return UserLoginResponse.builder().userInfoResponse(userInfo).jwtTokenResponse(tokens).build();
    }


}
