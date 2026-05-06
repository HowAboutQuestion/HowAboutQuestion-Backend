package com.howaboutquestion.backend.domain.examresult.entity;

import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * packageName    : com.howaboutquestion.backend.domain.examresult.entity<br>
 * fileName       : ExamResultEntity.java<br>
 * author         : khaelim1311 <br>
 * date           : 25.07.24<br>
 * description    : ExamResultEntity entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 * 26.04.30          eunchang             PostgreSQL 매핑 호환성 정리<br>
 */
@Entity
@Getter
@SuperBuilder
@Table(name = "tb_exam_result")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ExamResultEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(length = 31, nullable = false)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String picture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private ExamEntity exam;

    @Column(name = "check_correct", nullable = false)
    private Boolean checkCorrect;

    @Column(columnDefinition = "TEXT")
    private String tag;
}
