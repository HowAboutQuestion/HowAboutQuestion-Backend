
package com.howaboutquestion.backend.domain.dailyhistory.entity;

import com.howaboutquestion.backend.domain.usermeta.entity.UserMetaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * packageName    : com.howaboutquestion.backend.domain.dailyhistory.entity<br>
 * fileName       : DailyHistoryEntity.java<br>
 * author         : khaelim1311 <br>
 * date           : 25.07.24<br>
 * description    : DailyHistory entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_daily_history")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyHistoryEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "correct_question")
    private Integer correctQuestion;

    @Column(name = "solved_question")
    private Integer solvedQuestion;

    @Column(precision = 5, scale = 2)
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserMetaEntity user;
}
