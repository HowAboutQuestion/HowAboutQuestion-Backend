package com.howaboutquestion.backend.domain.book.service;

import com.howaboutquestion.backend.domain.book.dto.mapper.BookMapper;
import com.howaboutquestion.backend.domain.book.dto.request.BookCreateRequest;
import com.howaboutquestion.backend.domain.book.dto.request.BookUpdateRequest;
import com.howaboutquestion.backend.domain.book.dto.response.BookResponse;
import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.entity.Visibility;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.service.UserService;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookComplexServiceTest {

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookComplexService bookComplexService;

    @DisplayName("문제집 생성에 성공한다")
    @Test
    void createBookReturnsMappedResponse() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        BookCreateRequest request = BookCreateRequest.builder()
                .title("자료구조")
                .content("기본 정리")
                .visibility(Visibility.PRIVATE)
                .checkFavorite(false)
                .build();
        BookResponse response = createBookResponse();

        given(userService.findUserEntityById(1L)).willReturn(user);
        given(bookService.createBook(user, request)).willReturn(book);
        given(bookMapper.mapToBookResponse(book)).willReturn(response);

        BookResponse result = bookComplexService.createBook(1L, request);

        assertThat(result).isSameAs(response);
    }

    @DisplayName("내 문제집 목록 조회에 성공한다")
    @Test
    void getMyBooksReturnsMappedResponses() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        BookResponse response = createBookResponse();

        given(bookService.getMyBooks(1L)).willReturn(List.of(book));
        given(bookMapper.mapToBookResponse(book)).willReturn(response);

        List<BookResponse> results = bookComplexService.getMyBooks(1L);

        assertThat(results).containsExactly(response);
    }

    @DisplayName("내 문제집 상세 조회에 성공한다")
    @Test
    void getMyBookDetailReturnsMappedResponse() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        BookResponse response = createBookResponse();

        given(bookService.findBookById(10)).willReturn(book);
        given(bookMapper.mapToBookResponse(book)).willReturn(response);

        BookResponse result = bookComplexService.getMyBookDetail(1L, 10);

        assertThat(result).isSameAs(response);
    }

    @DisplayName("다른 사용자의 문제집 상세 조회는 권한 예외를 반환한다")
    @Test
    void getMyBookDetailThrowsWhenNotOwner() {
        UserEntity owner = createUserEntity(2);
        BookEntity book = createBookEntity(owner);
        given(bookService.findBookById(10)).willReturn(book);

        assertThatThrownBy(() -> bookComplexService.getMyBookDetail(1L, 10))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.NO_USER_PERMISSION);
    }

    @DisplayName("문제집 수정에 성공한다")
    @Test
    void updateBookReturnsMappedResponse() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        BookUpdateRequest request = BookUpdateRequest.builder()
                .title("알고리즘")
                .content("심화 정리")
                .visibility(Visibility.FRIEND)
                .checkFavorite(true)
                .build();
        BookResponse response = createBookResponse();

        given(bookService.findBookById(10)).willReturn(book);
        given(bookMapper.mapToBookResponse(book)).willReturn(response);

        BookResponse result = bookComplexService.updateBook(1L, 10, request);

        verify(bookService).updateBook(book, request);
        assertThat(result).isSameAs(response);
    }

    @DisplayName("문제집 삭제에 성공한다")
    @Test
    void deleteBookDeletesOwnedBook() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        given(bookService.findBookById(10)).willReturn(book);

        bookComplexService.deleteBook(1L, 10);

        verify(bookService).deleteBook(book);
    }

    private UserEntity createUserEntity(int id) {
        UserEntity user = UserEntity.builder()
                .email("user@example.com")
                .name("tester")
                .password("encoded-password")
                .profile("profile.png")
                .build();
        user.setId(id);
        return user;
    }

    private BookEntity createBookEntity(UserEntity user) {
        return BookEntity.builder()
                .id(10)
                .user(user)
                .title("자료구조")
                .content("기본 정리")
                .visibility(Visibility.PRIVATE)
                .checkFavorite(false)
                .createdAt(LocalDateTime.of(2026, 5, 6, 0, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 6, 1, 0))
                .build();
    }

    private BookResponse createBookResponse() {
        return BookResponse.builder()
                .id(10)
                .userId(1)
                .title("자료구조")
                .content("기본 정리")
                .visibility(Visibility.PRIVATE)
                .checkFavorite(false)
                .createdAt(LocalDateTime.of(2026, 5, 6, 0, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 6, 1, 0))
                .build();
    }
}
