package com.howaboutquestion.backend.domain.exam.controller;

import com.howaboutquestion.backend.domain.exam.dto.request.ExamAnswerSubmitRequest;
import com.howaboutquestion.backend.domain.exam.dto.request.ExamStartRequest;
import com.howaboutquestion.backend.domain.exam.service.ExamComplexService;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.controller<br>
 * fileName       : ExamController.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 요청을 처리하는 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 * 26.05.06          eunchang          답안 제출 및 결과 조회 엔드포인트 추가<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamComplexService examComplexService;

    @PostMapping
    public ResponseEntity<?> startExam(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid ExamStartRequest request
    ) {
        return ResponseUtility.success(
                examComplexService.startExam(Long.parseLong(userDetail.getUserId()), request),
                "시험이 시작되었습니다."
        );
    }

    @PostMapping("/{examId}/answer")
    public ResponseEntity<?> submitAnswers(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer examId,
            @RequestBody @Valid ExamAnswerSubmitRequest request
    ) {
        return ResponseUtility.success(
                examComplexService.submitAnswers(Long.parseLong(userDetail.getUserId()), examId, request),
                "답안이 제출되었습니다."
        );
    }

    @GetMapping("/{examId}/result")
    public ResponseEntity<?> getExamResult(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer examId
    ) {
        return ResponseUtility.success(
                examComplexService.getExamResult(Long.parseLong(userDetail.getUserId()), examId)
        );
    }
}
