package com.howaboutquestion.backend.domain.exam.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.dto.request<br>
 * fileName       : ExamStartRequest.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 시작 요청 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExamStartRequest {

    @NotNull
    private Integer bookId;
}
