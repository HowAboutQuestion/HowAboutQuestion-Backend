package com.howaboutquestion.backend.domain.question.controller;

import com.howaboutquestion.backend.domain.question.dto.request.QuestionCreateRequest;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionUpdateRequest;
import com.howaboutquestion.backend.domain.question.service.QuestionComplexService;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.controller<br>
 * fileName       : QuestionController.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제 요청을 처리하는 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 * 26.05.06          eunchang          QuestionComplexService 사용으로 변경<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionComplexService questionComplexService;

    @PostMapping
    public ResponseEntity<?> createQuestion(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid QuestionCreateRequest request
    ) {
        return ResponseUtility.success(
                questionComplexService.createQuestion(Long.parseLong(userDetail.getUserId()), request),
                "문제가 생성되었습니다."
        );
    }

    @GetMapping
    public ResponseEntity<?> getQuestions(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestParam Integer bookId
    ) {
        return ResponseUtility.success(questionComplexService.getQuestions(Long.parseLong(userDetail.getUserId()), bookId));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionDetail(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer questionId
    ) {
        return ResponseUtility.success(questionComplexService.getQuestionDetail(Long.parseLong(userDetail.getUserId()), questionId));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<?> updateQuestion(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer questionId,
            @RequestBody @Valid QuestionUpdateRequest request
    ) {
        return ResponseUtility.success(
                questionComplexService.updateQuestion(Long.parseLong(userDetail.getUserId()), questionId, request),
                "문제가 수정되었습니다."
        );
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer questionId
    ) {
        questionComplexService.deleteQuestion(Long.parseLong(userDetail.getUserId()), questionId);
        return ResponseUtility.success(null, "문제가 삭제되었습니다.");
    }
}
