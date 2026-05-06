package com.howaboutquestion.backend.domain.question.entity;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.questiontag.entity.QuestionTagEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * packageName    : com.howaboutquestion.backend.domain.question.entity<br>
 * fileName       : QuestionEntity.java<br>
 * author         : khaelim1311 <br>
 * date           : 25.07.24<br>
 * description    : Question(문제) entity 추상 클래스입니다. <br>
 *                  각 문제 유형(Multiple, Subjective)의 공통 필드를 정의합니다.
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 * 26.04.30          eunchang             PostgreSQL 매핑 호환성 정리<br>
 * 26.05.06          eunchang            문제 공통 수정 메서드 추가<br>
 */
@Entity
@Getter
@SuperBuilder
@Table(name = "tb_question")
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class QuestionEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "latest_at")
    private LocalDateTime latestAt;

    @Column(nullable = false)
    private String title;

    private String description;

    private String picture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @JoinColumn(name = "book_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private BookEntity book;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionTagEntity> tags = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (this.level == null) this.level = Level.ONE;
        if (this.type == null) this.type = QuestionType.MULTIPLE;
    }

    protected void updateCommonFields(String title, String description, String picture, Level level, QuestionType type) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.level = level;
        this.type = type;
    }
}
