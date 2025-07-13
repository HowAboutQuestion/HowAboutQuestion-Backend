package com.howaboutquestion.backend.domain.auth.controller;

import com.howaboutquestion.backend.domain.auth.dto.request.UserLoginRequest;
import com.howaboutquestion.backend.domain.auth.service.AuthService;
import com.howaboutquestion.backend.domain.auth.dto.request.UserRegisterRequest;
import com.howaboutquestion.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterRequest request){
        return ResponseUtility.success(authService.basicRegister(request), "회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest){
        return ResponseUtility.success();
    }
}
