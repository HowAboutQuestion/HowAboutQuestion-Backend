package com.howaboutquestion.backend.domain.user.service;

import com.howaboutquestion.backend.domain.auth.dto.request.UserLoginRequest;
import com.howaboutquestion.backend.domain.auth.dto.response.UserRegisterResponse;
import com.howaboutquestion.backend.domain.user.dto.mapper.UserMapper;
import com.howaboutquestion.backend.domain.auth.dto.request.UserRegisterRequest;
import com.howaboutquestion.backend.domain.user.dto.resopnse.UserInfoResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.repository.UserRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public UserRegisterResponse tryRegisterUser(UserRegisterRequest request){
        String email = request.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new CustomException(StatusCode.DUPLICATE_EMAIL);
        }

        return userMapper.mapToUserRegisterResponse(registerUser(request));
    }

    public UserEntity registerUser(UserRegisterRequest request){
        UserEntity entity = UserEntity.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        return userRepository.save(entity);
    }

    public UserInfoResponse tryUserLogin(UserLoginRequest login) {
        UserEntity origin = findByLoginEmail(login.getEmail());

        if(!checkPassword(login.getPassword(), origin.getPassword())){
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
        return userMapper.mapToUserInfoResponse(origin);
    }

    public boolean checkPassword(String loginPassword, String originPassword){
        return passwordEncoder.matches(loginPassword, originPassword);
    }
    @Transactional(readOnly = true)
    public UserEntity findByLoginEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(StatusCode.NOT_FOUND_USER));
    }




}
