package com.howaboutquestion.backend.domain.exam.entity;

import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.entity<br>
 * fileName       : ExamEntity.java<br>
 * author         : khaelim1311 <br>
 * date           : 25.07.24<br>
 * description    : Exam entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 * 26.04.30          cod0216             PostgreSQL 매핑 호환성 정리<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_exam")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_history_id", nullable = false)
    private DailyHistoryEntity dailyHistory;

    @Column(name = "correct_question", nullable = false)
    private Integer correctQuestion;

    @Column(name = "solved_question", nullable = false)
    private Integer solvedQuestion;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;

    @Column(columnDefinition = "TEXT")
    private String tag;

    @Column(name = "book_id", nullable = false)
    private Integer bookId;
}
