package com.howaboutquestion.backend.domain.dashboard.dto.response;

import com.howaboutquestion.backend.domain.history.dto.response.ExamHistorySummaryResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.dashboard.dto.response<br>
 * fileName       : DashboardSummaryResponse.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 대시보드 요약 응답 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DashboardSummaryResponse {

    private Integer todaySolvedQuestion;
    private Integer todayCorrectQuestion;
    private BigDecimal todayRate;
    private List<ExamHistorySummaryResponse> recentExams;
}
