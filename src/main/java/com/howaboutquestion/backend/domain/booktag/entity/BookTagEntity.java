package com.howaboutquestion.backend.domain.booktag.entity;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.tag.entity.TagEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * packageName    : com.howaboutquestion.backend.domain.booktag.entity<br>
 * fileName       : BookTagEntity.java<br>
 * author         : khaelim1311 <br>
 * date           : 2025-07-24<br>
 * description    : BookTagEntity entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_book_tag")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookTagEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private BookTagId id;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;
}
