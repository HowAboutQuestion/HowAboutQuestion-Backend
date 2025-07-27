package com.howaboutquestion.backend.domain.questiontag.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * packageName    : com.howaboutquestion.backend.domain.questiontag.entity<br>
 * fileName       : QuestionTagId.java<br>
 * author         : khaelim1311 <br>
 * date           : 2025-07-24<br>
 * description    : QuestionTag 관계 테이블의 식별자(QuestionTagId)를 위한 클래스입니다. <br>
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
public class QuestionTagId implements Serializable {
    private Long tagId;
    private Integer questionId;
}
