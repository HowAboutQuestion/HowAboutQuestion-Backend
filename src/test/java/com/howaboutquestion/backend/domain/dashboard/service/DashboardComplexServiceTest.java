package com.howaboutquestion.backend.domain.dashboard.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.entity.Visibility;
import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import com.howaboutquestion.backend.domain.dashboard.dto.response.DashboardSummaryResponse;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DashboardComplexServiceTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardComplexService dashboardComplexService;

    @DisplayName("대시보드 요약 조회에 성공한다")
    @Test
    void getDashboardSummaryReturnsTodayStatsAndRecentExams() {
        UserEntity user = createUserEntity();
        DailyHistoryEntity todayHistory = createDailyHistory(user);
        ExamEntity exam = createExamEntity(user, 100);

        given(dashboardService.getTodayHistory(1L)).willReturn(Optional.of(todayHistory));
        given(dashboardService.getRecentExams(1L)).willReturn(List.of(exam));

        DashboardSummaryResponse response = dashboardComplexService.getDashboardSummary(1L);

        assertThat(response.getTodaySolvedQuestion()).isEqualTo(10);
        assertThat(response.getTodayCorrectQuestion()).isEqualTo(7);
        assertThat(response.getTodayRate()).isEqualTo(BigDecimal.valueOf(70.00));
        assertThat(response.getRecentExams()).hasSize(1);
        assertThat(response.getRecentExams().get(0).getExamId()).isEqualTo(100);
    }

    @DisplayName("기록이 없는 사용자는 0값과 빈 최근 기록을 반환한다")
    @Test
    void getDashboardSummaryReturnsDefaultValuesWhenNoData() {
        given(dashboardService.getTodayHistory(1L)).willReturn(Optional.empty());
        given(dashboardService.getRecentExams(1L)).willReturn(List.of());

        DashboardSummaryResponse response = dashboardComplexService.getDashboardSummary(1L);

        assertThat(response.getTodaySolvedQuestion()).isZero();
        assertThat(response.getTodayCorrectQuestion()).isZero();
        assertThat(response.getTodayRate()).isEqualTo(BigDecimal.ZERO);
        assertThat(response.getRecentExams()).isEmpty();
    }

    private UserEntity createUserEntity() {
        UserEntity user = UserEntity.builder()
                .email("user@example.com")
                .name("tester")
                .password("encoded-password")
                .profile("profile.png")
                .build();
        user.setId(1);
        return user;
    }

    private DailyHistoryEntity createDailyHistory(UserEntity user) {
        return DailyHistoryEntity.builder()
                .id(20)
                .user(user)
                .date(LocalDate.of(2026, 5, 6))
                .correctQuestion(7)
                .solvedQuestion(10)
                .rate(BigDecimal.valueOf(70.00))
                .build();
    }

    private ExamEntity createExamEntity(UserEntity user, Integer examId) {
        BookEntity book = BookEntity.builder()
                .id(10)
                .user(user)
                .title("자료구조")
                .content("기본 정리")
                .visibility(Visibility.PRIVATE)
                .checkFavorite(false)
                .createdAt(LocalDateTime.of(2026, 5, 6, 0, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 6, 1, 0))
                .build();

        return ExamEntity.builder()
                .id(examId)
                .createdAt(LocalDateTime.of(2026, 5, 6, 10, 0))
                .book(book)
                .correctQuestion(1)
                .solvedQuestion(2)
                .rate(BigDecimal.valueOf(50.00))
                .build();
    }
}
