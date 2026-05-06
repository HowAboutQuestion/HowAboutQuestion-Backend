package com.howaboutquestion.backend.domain.exam.dto.response;

import com.howaboutquestion.backend.domain.question.entity.QuestionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.dto.response<br>
 * fileName       : ExamStartQuestionResponse.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 시작 시 반환할 문제 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExamStartQuestionResponse {

    private Integer questionId;
    private QuestionType type;
    private String title;
    private String description;
    private String picture;
    private String selectOne;
    private String selectTwo;
    private String selectThree;
    private String selectFour;
    private String selectFive;
}
