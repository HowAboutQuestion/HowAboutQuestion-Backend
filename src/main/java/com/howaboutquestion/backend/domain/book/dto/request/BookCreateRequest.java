package com.howaboutquestion.backend.domain.book.dto.request;

import com.howaboutquestion.backend.domain.book.entity.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.howaboutquestion.backend.domain.book.dto.request<br>
 * fileName       : BookCreateRequest.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제집 생성 요청 DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookCreateRequest {

    @NotBlank
    private String title;

    private String content;

    @NotNull
    private Visibility visibility;

    @NotNull
    private Boolean checkFavorite;
}
