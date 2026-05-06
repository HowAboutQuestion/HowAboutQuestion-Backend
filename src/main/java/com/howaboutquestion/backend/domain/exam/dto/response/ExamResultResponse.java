package com.howaboutquestion.backend.domain.exam.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.dto.response<br>
 * fileName       : ExamResultResponse.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 결과 조회 응답 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExamResultResponse {

    private Integer examId;
    private Integer bookId;
    private String bookTitle;
    private LocalDateTime startedAt;
    private Integer correctQuestion;
    private Integer solvedQuestion;
    private BigDecimal rate;
    private List<ExamResultItemResponse> results;
}
