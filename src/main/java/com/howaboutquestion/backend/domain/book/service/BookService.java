package com.howaboutquestion.backend.domain.book.service;

import com.howaboutquestion.backend.domain.book.dto.request.BookUpdateRequest;
import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.repository.BookRepository;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
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
 * description    : 문제집 도메인 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 * 26.05.06          eunchang          ComplexService 하위 도메인 서비스로 역할 정리<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookEntity createBook(UserEntity user, com.howaboutquestion.backend.domain.book.dto.request.BookCreateRequest request) {
        return bookRepository.save(BookEntity.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .visibility(request.getVisibility())
                .checkFavorite(request.getCheckFavorite())
                .build());
    }

    @Transactional(readOnly = true)
    public List<BookEntity> getMyBooks(Long userId) {
        return bookRepository.findAllByUserIdOrderByCreatedAtDesc(Math.toIntExact(userId));
    }

    @Transactional(readOnly = true)
    public BookEntity findBookById(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND));
    }

    public void updateBook(BookEntity book, BookUpdateRequest request) {
        book.updateBook(request.getTitle(), request.getContent(), request.getVisibility(), request.getCheckFavorite());
    }

    public void deleteBook(BookEntity book) {
        bookRepository.delete(book);
    }
}
