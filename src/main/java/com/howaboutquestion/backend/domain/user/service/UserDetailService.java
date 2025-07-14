package com.howaboutquestion.backend.domain.user.service;

import com.howaboutquestion.backend.domain.user.dto.mapper.UserMapper;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.repository.UserRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.howaboutquestion.backend.domain.user.service<br>
 * fileName       : UserDetailService.java<br>
 * author         : cod0216 <br>
 * date           : 2025.07.13<br>
 * description    : USER 관련 서비스 로직을 수행하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초 생성 <br>
 */
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * 토큰의 userId 값으로 해당 Id의 회원을 조회합니다.
     * @param userId 토큰 페이로더의 userId
     * @return 인증 처리에 사용될 사용자 정보
     * @throws CustomException 토큰의 userId로 사용자 정보 조회 실패 시 에러 반환
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new CustomException(StatusCode.INVALID_TOKEN));

        return userMapper.mapToUserDetail(user);
    }
}
