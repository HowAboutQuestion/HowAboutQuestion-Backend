package com.howaboutquestion.backend.domain.dashboard.service;

import com.howaboutquestion.backend.domain.dashboard.dto.response.DashboardSummaryResponse;
import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.history.dto.response.ExamHistorySummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.dashboard.service<br>
 * fileName       : DashboardComplexService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 대시보드 유스케이스를 조합하는 ComplexService 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardComplexService {

    private final DashboardService dashboardService;

    public DashboardSummaryResponse getDashboardSummary(Long userId) {
        DailyHistoryEntity todayHistory = dashboardService.getTodayHistory(userId).orElse(null);
        List<ExamHistorySummaryResponse> recentExams = dashboardService.getRecentExams(userId)
                .stream()
                .map(this::toSummaryResponse)
                .toList();

        return DashboardSummaryResponse.builder()
                .todaySolvedQuestion(todayHistory != null ? todayHistory.getSolvedQuestion() : 0)
                .todayCorrectQuestion(todayHistory != null ? todayHistory.getCorrectQuestion() : 0)
                .todayRate(todayHistory != null && todayHistory.getRate() != null ? todayHistory.getRate() : BigDecimal.ZERO)
                .recentExams(recentExams)
                .build();
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
