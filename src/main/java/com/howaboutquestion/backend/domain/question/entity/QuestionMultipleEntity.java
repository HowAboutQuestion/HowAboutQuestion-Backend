
package com.howaboutquestion.backend.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.entity<br>
 * fileName       : QuestionMultipleEntity.java<br>
 * author         : khaelim1311<br>
 * date           : 25.07.24<br>
 * description    : QuestionMultiple Entity 클래스입니다<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311        최초생성<br>
 * 26.05.06          eunchang           상속 빌더 및 수정 메서드 추가<br>
 */
@Entity
@Getter
@SuperBuilder
@Table(name = "tb_question_multiple")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionMultipleEntity extends QuestionEntity {

    @Column(name = "select_one", length = 255, nullable = false)
    private String selectOne;

    @Column(name = "select_two", length = 255)
    private String selectTwo;

    @Column(name = "select_three", length = 255)
    private String selectThree;

    @Column(name = "select_four", length = 255)
    private String selectFour;

    @Column(name = "select_five", length = 255)
    private String selectFive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 255)
    private MultipleAnswer answer;

    public void updateQuestion(
            String title,
            String description,
            String picture,
            Level level,
            String selectOne,
            String selectTwo,
            String selectThree,
            String selectFour,
            String selectFive,
            MultipleAnswer answer
    ) {
        updateCommonFields(title, description, picture, level, QuestionType.MULTIPLE);
        this.selectOne = selectOne;
        this.selectTwo = selectTwo;
        this.selectThree = selectThree;
        this.selectFour = selectFour;
        this.selectFive = selectFive;
        this.answer = answer;
    }
}
