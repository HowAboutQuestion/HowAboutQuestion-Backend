package com.howaboutquestion.backend.domain.book.controller;

import com.howaboutquestion.backend.domain.book.dto.request.BookCreateRequest;
import com.howaboutquestion.backend.domain.book.dto.request.BookUpdateRequest;
import com.howaboutquestion.backend.domain.book.service.BookService;
import com.howaboutquestion.backend.domain.user.dto.UserDetail;
import com.howaboutquestion.backend.global.util.ResponseUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.howaboutquestion.backend.domain.book.controller<br>
 * fileName       : BookController.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제집 요청을 처리하는 Controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<?> createBook(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid BookCreateRequest request
    ) {
        return ResponseUtility.success(
                bookService.createBook(Long.parseLong(userDetail.getUserId()), request),
                "문제집이 생성되었습니다."
        );
    }

    @GetMapping
    public ResponseEntity<?> getMyBooks(@AuthenticationPrincipal UserDetail userDetail) {
        return ResponseUtility.success(bookService.getMyBooks(Long.parseLong(userDetail.getUserId())));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getMyBookDetail(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer bookId
    ) {
        return ResponseUtility.success(bookService.getMyBookDetail(Long.parseLong(userDetail.getUserId()), bookId));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer bookId,
            @RequestBody @Valid BookUpdateRequest request
    ) {
        return ResponseUtility.success(
                bookService.updateBook(Long.parseLong(userDetail.getUserId()), bookId, request),
                "문제집이 수정되었습니다."
        );
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Integer bookId
    ) {
        bookService.deleteBook(Long.parseLong(userDetail.getUserId()), bookId);
        return ResponseUtility.success(null, "문제집이 삭제되었습니다.");
    }
}
