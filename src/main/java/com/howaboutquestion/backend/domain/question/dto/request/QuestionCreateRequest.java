package com.howaboutquestion.backend.domain.question.dto.request;

import com.howaboutquestion.backend.domain.question.entity.Level;
import com.howaboutquestion.backend.domain.question.entity.MultipleAnswer;
import com.howaboutquestion.backend.domain.question.entity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.dto.request<br>
 * fileName       : QuestionCreateRequest.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제 생성 요청 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionCreateRequest {

    @NotNull
    private Integer bookId;

    @NotBlank
    private String title;

    private String description;

    private String picture;

    @NotNull
    private Level level;

    @NotNull
    private QuestionType type;

    private String selectOne;
    private String selectTwo;
    private String selectThree;
    private String selectFour;
    private String selectFive;
    private MultipleAnswer multipleAnswer;
    private String subjectiveAnswer;
}
