package com.howaboutquestion.backend.domain.booktag.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * packageName    : com.howaboutquestion.backend.domain.booktag.entity<br>
 * fileName       : BookTagId.java<br>
 * author         : khaelim1311 <br>
 * date           : 2025-07-24<br>
 * description    : BookTag 관계 테이블의 식별자(BookTagId)를 위한 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookTagId implements Serializable  {
    private Long tagId;
    private Integer bookId;
}

