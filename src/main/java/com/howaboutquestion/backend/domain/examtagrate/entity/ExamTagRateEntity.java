package com.howaboutquestion.backend.domain.examtagrate.entity;

import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * packageName    : com.howaboutquestion.backend.domain.examtagrate.entity<br>
 * fileName       : ExamTagRateEntity.java<br>
 * author         : khaelim1311 <br>
 * date           : 2025-07-24<br>
 * description    : ExamTagRate entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_exam_tag_rate")
@EntityListeners(EntityListeners.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamTagRateEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private ExamEntity exam;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "correct_question", nullable = false)
    private Integer correctQuestion;

    @Column(name = "solved_question", nullable = false)
    private Integer solvedQuestion;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;

    /**
     * 정답률 계산 메서드
     * solvedQuestion 이 0일 경우 0.00으로 설정
     */
    public void calculateRate() {
        if (solvedQuestion == null || solvedQuestion == 0 || correctQuestion == null) {
            this.rate = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            return;
        }

        this.rate = BigDecimal.valueOf(correctQuestion)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(solvedQuestion), 2, RoundingMode.HALF_UP);
    }
}
