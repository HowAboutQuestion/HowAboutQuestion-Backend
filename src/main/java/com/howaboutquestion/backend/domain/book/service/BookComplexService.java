package com.howaboutquestion.backend.domain.book.service;

import com.howaboutquestion.backend.domain.book.dto.mapper.BookMapper;
import com.howaboutquestion.backend.domain.book.dto.request.BookCreateRequest;
import com.howaboutquestion.backend.domain.book.dto.request.BookUpdateRequest;
import com.howaboutquestion.backend.domain.book.dto.response.BookResponse;
import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.service.UserService;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.book.service<br>
 * fileName       : BookComplexService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제집 유스케이스를 조합하는 ComplexService 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookComplexService {

    private final BookService bookService;
    private final UserService userService;
    private final BookMapper bookMapper;

    public BookResponse createBook(Long userId, BookCreateRequest request) {
        UserEntity user = userService.findUserEntityById(userId);
        BookEntity book = bookService.createBook(user, request);
        return bookMapper.mapToBookResponse(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getMyBooks(Long userId) {
        return bookService.getMyBooks(userId)
                .stream()
                .map(bookMapper::mapToBookResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse getMyBookDetail(Long userId, Integer bookId) {
        return bookMapper.mapToBookResponse(findOwnedBook(userId, bookId));
    }

    public BookResponse updateBook(Long userId, Integer bookId, BookUpdateRequest request) {
        BookEntity book = findOwnedBook(userId, bookId);
        bookService.updateBook(book, request);
        return bookMapper.mapToBookResponse(book);
    }

    public void deleteBook(Long userId, Integer bookId) {
        BookEntity book = findOwnedBook(userId, bookId);
        bookService.deleteBook(book);
    }

    @Transactional(readOnly = true)
    public BookEntity findOwnedBook(Long userId, Integer bookId) {
        BookEntity book = bookService.findBookById(bookId);

        if (!book.getUser().getId().equals(Math.toIntExact(userId))) {
            throw new CustomException(StatusCode.NO_USER_PERMISSION);
        }

        return book;
    }
}
