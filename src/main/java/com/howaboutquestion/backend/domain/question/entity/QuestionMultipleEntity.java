
package com.howaboutquestion.backend.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.entity<br>
 * fileName       : QuestionMultipleEntity.java<br>
 * author         : khaelim1311<br>
 * date           : 2025-07-24<br>
 * description    : QuestionMultiple Entity 클래스입니다<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311        최초생성<br>
 */
@Entity
@Getter
@Builder
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

}
