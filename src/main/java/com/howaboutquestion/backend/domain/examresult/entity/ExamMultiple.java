
package com.howaboutquestion.backend.domain.examresult.entity;

import com.howaboutquestion.backend.domain.question.entity.MultipleAnswer;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.howaboutquestion.backend.domain.examresult.entity<br>
 * fileName       : ExamMultiple.java<br>
 * author         : khaelim1311 <br>
 * date           : 25.07.24<br>
 * description    : ExamMultiple entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_exam_multiple")
@PrimaryKeyJoinColumn(name = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamMultiple extends ExamResultEntity {

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
