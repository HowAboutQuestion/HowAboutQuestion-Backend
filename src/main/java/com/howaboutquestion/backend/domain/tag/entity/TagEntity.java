package com.howaboutquestion.backend.domain.tag.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.howaboutquestion.backend.domain.tag.entity<br>
 * fileName       : TagEntity.java<br>
 * author         : khaelim1311 <br>
 * date           : 2025-07-24<br>
 * description    : Tag Entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.24          khaelim1311         최초생성<br>
 */
@Entity
@Getter
@Builder
@Table(name = "tb_tag")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(nullable = false, length = 31, unique = true)
    private String name;
}
