package com.howaboutquestion.backend.domain.examresult.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * packageName    : com.howaboutquestion.backend.domain.examresult.entity<br>
 * fileName       : ExamSubjective.java<br>
 * author         : khaelim1311 <br>
 * date           : 2025-07-24<br>
 * description    : ExamSubjective entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 */
@Entity
@Getter
@Builder
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "tb_exam_subjective")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamSubjective extends ExamResultEntity {

    @Column(nullable = false, length = 255)
    private String answer;
}
