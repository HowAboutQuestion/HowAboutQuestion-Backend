package com.howaboutquestion.backend.domain.user.controller;

import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.domain.user.service.UserService;
import com.howaboutquestion.backend.global.util.ResponseUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.howaboutquestion.backend.domain.user.controller<br>
 * fileName       : UserController.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 사용자 조회 요청을 처리하는 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 로그인한 사용자의 프로필을 조회합니다.
     * @param userDetail 인증된 사용자 정보
     * @return 사용자 프로필 정보
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetail userDetail) {
        return ResponseUtility.success(userService.getCurrentUserInfo(Long.parseLong(userDetail.getUserId())));
    }
}
