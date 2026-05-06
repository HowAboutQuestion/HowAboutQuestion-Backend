package com.howaboutquestion.backend.domain.book.dto.response;

import com.howaboutquestion.backend.domain.book.entity.Visibility;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.howaboutquestion.backend.domain.book.dto.response<br>
 * fileName       : BookResponse.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제집 응답 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookResponse {

    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private Visibility visibility;
    private Boolean checkFavorite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
