package com.howaboutquestion.backend.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.entity<br>
 * fileName       : QuestionSubjective.java<br>
 * author         : khaelim1311<br>
 * date           : 25.07.24<br>
 * description    : QuestionSubjective Entity 클래스입니다<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311        최초생성<br>
 * 26.05.06          eunchang           상속 빌더 및 수정 메서드 추가<br>
 */
@Entity
@Getter
@SuperBuilder
@Table(name = "tb_question_subjective")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSubjectiveEntity extends QuestionEntity {

    @Column(nullable = false)
    private String answer;

    public void updateQuestion(
            String title,
            String description,
            String picture,
            Level level,
            String answer
    ) {
        updateCommonFields(title, description, picture, level, QuestionType.SUBJECTIVE);
        this.answer = answer;
    }
}
