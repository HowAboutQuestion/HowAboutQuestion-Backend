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

/**
 * packageName    : com.howaboutquestion.backend.domain.user.service<br>
 * fileName       : UserService.java<br>
 * author         : eunchang <br>
 * date           : 2025.07.13<br>
 * description    : USER 관련 서비스 로직을 수행하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          eunchang           최초 생성 <br>
 * 26.05.06          eunchang           현재 사용자 프로필 조회 로직 추가<br>
 */

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    /**
     * 회원 가입을 시도합니다.
     * @param request 회원 가입 요청 정보
     * @return 회원 정보
     */
    public UserRegisterResponse tryRegisterUser(UserRegisterRequest request){
        String email = request.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new CustomException(StatusCode.DUPLICATE_EMAIL);
        }

        return userMapper.mapToUserRegisterResponse(registerUser(request));
    }

    /**
     * 회원 가입을 수행합니다.
     * @param request 회원 가입 요청 정보
     * @return 회원 Entity
     */
    public UserEntity registerUser(UserRegisterRequest request){
        UserEntity entity = UserEntity.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        return userRepository.save(entity);
    }

    /**
     * 로그인을 시도합니다.
     * @param login 로그인 요청 정보
     * @return 회원 정보
     */
    public UserInfoResponse tryUserLogin(UserLoginRequest login) {
        UserEntity origin = findByLoginEmail(login.getEmail());

        if(!checkPassword(login.getPassword(), origin.getPassword())){
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
        return userMapper.mapToUserInfoResponse(origin);
    }

    /**
     * 현재 로그인한 사용자의 정보를 조회합니다.
     * @param userId 로그인한 사용자 Id
     * @return 사용자 정보
     */
    @Transactional(readOnly = true)
    public UserInfoResponse getCurrentUserInfo(Long userId) {
        UserEntity user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_USER));

        return userMapper.mapToUserInfoResponse(user);
    }

    /**
     * 요청한 비밀번호와 암호화된 비밀번호를 비교합니다.
     * @param loginPassword 로그인 시 비밀번호
     * @param originPassword 암호화된 비밀번호
     * @return 일치, 불일치 여부
     */
    public boolean checkPassword(String loginPassword, String originPassword){
        return passwordEncoder.matches(loginPassword, originPassword);
    }

    /**
     * 회원가입 시 해당 이메일로 가입한 여부 검사
     * @param email 회원가입 시 입력한 이메일
     * @return 존재 여부 반환
     */
    @Transactional(readOnly = true)
    public UserEntity findByLoginEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()
                -> new CustomException(StatusCode.NOT_FOUND_USER));
    }
}
