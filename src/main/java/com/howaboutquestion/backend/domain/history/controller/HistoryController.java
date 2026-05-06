package com.howaboutquestion.backend.domain.history.controller;

import com.howaboutquestion.backend.domain.history.service.HistoryComplexService;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.global.util.ResponseUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.howaboutquestion.backend.domain.history.controller<br>
 * fileName       : HistoryController.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 히스토리 요청을 처리하는 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history/exams")
public class HistoryController {

    private final HistoryComplexService historyComplexService;

    @GetMapping
    public ResponseEntity<?> getExamHistories(@AuthenticationPrincipal UserDetail userDetail) {
        return ResponseUtility.success(
                historyComplexService.getExamHistories(Long.parseLong(userDetail.getUserId()))
        );
    }

    @GetMapping("/{examId}")
    public ResponseEntity<?> getExamHistoryDetail(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer examId
    ) {
        return ResponseUtility.success(
                historyComplexService.getExamHistoryDetail(Long.parseLong(userDetail.getUserId()), examId)
        );
    }
}
