package com.howaboutquestion.backend.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;

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
 */
@Entity
@Getter
@Builder
@Table(name = "tb_question_subjective")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSubjectiveEntity extends QuestionEntity {

    @Column(nullable = false)
    private String answer;
}
