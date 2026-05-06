package com.howaboutquestion.backend.domain.history.service;

import com.howaboutquestion.backend.domain.exam.dto.mapper.ExamMapper;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamResultItemResponse;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamResultResponse;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.examresult.service.ExamResultService;
import com.howaboutquestion.backend.domain.history.dto.response.ExamHistorySummaryResponse;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.history.service<br>
 * fileName       : HistoryComplexService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 히스토리 유스케이스를 조합하는 ComplexService 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryComplexService {

    private final HistoryService historyService;
    private final ExamResultService examResultService;
    private final ExamMapper examMapper;

    public List<ExamHistorySummaryResponse> getExamHistories(Long userId) {
        return historyService.getExamHistories(userId)
                .stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    public ExamResultResponse getExamHistoryDetail(Long userId, Integer examId) {
        ExamEntity exam = findOwnedExam(userId, examId);
        List<ExamResultItemResponse> resultItems = examResultService.getExamResults(examId)
                .stream()
                .map(examMapper::mapToExamResultItemResponse)
                .toList();

        return ExamResultResponse.builder()
                .examId(exam.getId())
                .bookId(exam.getBook().getId())
                .bookTitle(exam.getBook().getTitle())
                .startedAt(exam.getCreatedAt())
                .correctQuestion(exam.getCorrectQuestion())
                .solvedQuestion(exam.getSolvedQuestion())
                .rate(exam.getRate())
                .results(resultItems)
                .build();
    }

    public ExamEntity findOwnedExam(Long userId, Integer examId) {
        ExamEntity exam = historyService.findExamById(examId);

        if (!exam.getBook().getUser().getId().equals(Math.toIntExact(userId))) {
            throw new CustomException(StatusCode.NO_USER_PERMISSION);
        }

        return exam;
    }

    private ExamHistorySummaryResponse toSummaryResponse(ExamEntity exam) {
        return ExamHistorySummaryResponse.builder()
                .examId(exam.getId())
                .bookId(exam.getBook().getId())
                .bookTitle(exam.getBook().getTitle())
                .createdAt(exam.getCreatedAt())
                .correctQuestion(exam.getCorrectQuestion())
                .solvedQuestion(exam.getSolvedQuestion())
                .rate(exam.getRate())
                .build();
    }
}
