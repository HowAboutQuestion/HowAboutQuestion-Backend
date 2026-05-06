package com.howaboutquestion.backend.domain.book.service;

import com.howaboutquestion.backend.domain.book.dto.request.BookCreateRequest;
import com.howaboutquestion.backend.domain.book.dto.request.BookUpdateRequest;
import com.howaboutquestion.backend.domain.book.dto.response.BookResponse;
import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.repository.BookRepository;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.repository.UserRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.book.service<br>
 * fileName       : BookService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제집 관련 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookResponse createBook(Long userId, BookCreateRequest request) {
        UserEntity user = findUserById(userId);

        BookEntity savedBook = bookRepository.save(BookEntity.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .visibility(request.getVisibility())
                .checkFavorite(request.getCheckFavorite())
                .build());

        return toResponse(savedBook);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getMyBooks(Long userId) {
        return bookRepository.findAllByUserIdOrderByCreatedAtDesc(Math.toIntExact(userId))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse getMyBookDetail(Long userId, Integer bookId) {
        return toResponse(findOwnedBook(userId, bookId));
    }

    public BookResponse updateBook(Long userId, Integer bookId, BookUpdateRequest request) {
        BookEntity book = findOwnedBook(userId, bookId);
        book.updateBook(request.getTitle(), request.getContent(), request.getVisibility(), request.getCheckFavorite());
        return toResponse(book);
    }

    public void deleteBook(Long userId, Integer bookId) {
        BookEntity book = findOwnedBook(userId, bookId);
        bookRepository.delete(book);
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_USER));
    }

    private BookEntity findOwnedBook(Long userId, Integer bookId) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND));

        if (!book.getUser().getId().equals(Math.toIntExact(userId))) {
            throw new CustomException(StatusCode.NO_USER_PERMISSION);
        }

        return book;
    }

    private BookResponse toResponse(BookEntity book) {
        return BookResponse.builder()
                .id(book.getId())
                .userId(book.getUser().getId())
                .title(book.getTitle())
                .content(book.getContent())
                .visibility(book.getVisibility())
                .checkFavorite(book.getCheckFavorite())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
