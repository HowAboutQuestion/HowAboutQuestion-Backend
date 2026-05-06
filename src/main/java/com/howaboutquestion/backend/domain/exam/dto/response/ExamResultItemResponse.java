package com.howaboutquestion.backend.domain.exam.dto.response;

import com.howaboutquestion.backend.domain.question.entity.MultipleAnswer;
import com.howaboutquestion.backend.domain.question.entity.QuestionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.dto.response<br>
 * fileName       : ExamResultItemResponse.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 결과 문제별 응답 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExamResultItemResponse {

    private Integer examResultId;
    private QuestionType type;
    private String title;
    private String description;
    private String picture;
    private Boolean checkCorrect;
    private String selectOne;
    private String selectTwo;
    private String selectThree;
    private String selectFour;
    private String selectFive;
    private MultipleAnswer multipleAnswer;
    private String subjectiveAnswer;
}
