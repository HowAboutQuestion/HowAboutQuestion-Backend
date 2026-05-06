package com.howaboutquestion.backend.domain.dashboard.controller;

import com.howaboutquestion.backend.domain.dashboard.service.DashboardComplexService;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.global.util.ResponseUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.howaboutquestion.backend.domain.dashboard.controller<br>
 * fileName       : DashboardController.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 대시보드 요청을 처리하는 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardComplexService dashboardComplexService;

    @GetMapping("/summary")
    public ResponseEntity<?> getDashboardSummary(@AuthenticationPrincipal UserDetail userDetail) {
        return ResponseUtility.success(
                dashboardComplexService.getDashboardSummary(Long.parseLong(userDetail.getUserId()))
        );
    }
}
