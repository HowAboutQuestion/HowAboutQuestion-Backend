package com.howaboutquestion.backend.domain.question.dto.response;

import com.howaboutquestion.backend.domain.question.entity.Level;
import com.howaboutquestion.backend.domain.question.entity.MultipleAnswer;
import com.howaboutquestion.backend.domain.question.entity.QuestionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.dto.response<br>
 * fileName       : QuestionResponse.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제 응답 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionResponse {

    private Integer id;
    private Integer bookId;
    private String title;
    private String description;
    private String picture;
    private Level level;
    private QuestionType type;
    private String selectOne;
    private String selectTwo;
    private String selectThree;
    private String selectFour;
    private String selectFive;
    private MultipleAnswer multipleAnswer;
    private String subjectiveAnswer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
