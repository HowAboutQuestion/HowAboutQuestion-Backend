package com.howaboutquestion.backend.domain.book.service;

import com.howaboutquestion.backend.domain.book.dto.request.BookCreateRequest;
import com.howaboutquestion.backend.domain.book.dto.request.BookUpdateRequest;
import com.howaboutquestion.backend.domain.book.dto.response.BookResponse;
import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.entity.Visibility;
import com.howaboutquestion.backend.domain.book.repository.BookRepository;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.repository.UserRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookService bookService;

    @DisplayName("문제집 생성에 성공한다")
    @Test
    void createBookReturnsSavedBook() {
        UserEntity user = createUserEntity();
        BookEntity savedBook = createBookEntity(user);
        BookCreateRequest request = BookCreateRequest.builder()
                .title("자료구조")
                .content("기본 정리")
                .visibility(Visibility.PRIVATE)
                .checkFavorite(false)
                .build();

        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(bookRepository.save(any(BookEntity.class))).willReturn(savedBook);

        BookResponse response = bookService.createBook(1L, request);

        assertThat(response.getId()).isEqualTo(10);
        assertThat(response.getTitle()).isEqualTo("자료구조");
        assertThat(response.getUserId()).isEqualTo(1);
    }

    @DisplayName("내 문제집 목록을 생성일 내림차순으로 조회한다")
    @Test
    void getMyBooksReturnsMappedResponses() {
        UserEntity user = createUserEntity();
        given(bookRepository.findAllByUserIdOrderByCreatedAtDesc(1)).willReturn(List.of(createBookEntity(user)));

        List<BookResponse> responses = bookService.getMyBooks(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTitle()).isEqualTo("자료구조");
    }

    @DisplayName("내 문제집 상세 조회에 성공한다")
    @Test
    void getMyBookDetailReturnsBookResponse() {
        UserEntity user = createUserEntity();
        given(bookRepository.findById(10)).willReturn(Optional.of(createBookEntity(user)));

        BookResponse response = bookService.getMyBookDetail(1L, 10);

        assertThat(response.getId()).isEqualTo(10);
        assertThat(response.getTitle()).isEqualTo("자료구조");
    }

    @DisplayName("내 문제집 수정에 성공한다")
    @Test
    void updateBookUpdatesEntityFields() {
        UserEntity user = createUserEntity();
        BookEntity book = createBookEntity(user);
        BookUpdateRequest request = BookUpdateRequest.builder()
                .title("알고리즘")
                .content("심화 정리")
                .visibility(Visibility.FRIEND)
                .checkFavorite(true)
                .build();

        given(bookRepository.findById(10)).willReturn(Optional.of(book));

        BookResponse response = bookService.updateBook(1L, 10, request);

        assertThat(response.getTitle()).isEqualTo("알고리즘");
        assertThat(response.getContent()).isEqualTo("심화 정리");
        assertThat(response.getVisibility()).isEqualTo(Visibility.FRIEND);
        assertThat(response.getCheckFavorite()).isTrue();
    }

    @DisplayName("다른 사용자의 문제집을 수정하려고 하면 권한 예외를 반환한다")
    @Test
    void updateBookThrowsWhenBookIsNotOwnedByUser() {
        UserEntity owner = UserEntity.builder()
                .email("other@example.com")
                .name("other")
                .password("encoded-password")
                .profile("profile.png")
                .build();
        owner.setId(2);

        BookUpdateRequest request = BookUpdateRequest.builder()
                .title("알고리즘")
                .content("심화 정리")
                .visibility(Visibility.FRIEND)
                .checkFavorite(true)
                .build();
        given(bookRepository.findById(10)).willReturn(Optional.of(createBookEntity(owner)));

        assertThatThrownBy(() -> bookService.updateBook(1L, 10, request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.NO_USER_PERMISSION);
    }

    @DisplayName("내 문제집 삭제에 성공한다")
    @Test
    void deleteBookDeletesOwnedBook() {
        UserEntity user = createUserEntity();
        BookEntity book = createBookEntity(user);
        given(bookRepository.findById(10)).willReturn(Optional.of(book));

        bookService.deleteBook(1L, 10);

        verify(bookRepository).delete(book);
    }

    private UserEntity createUserEntity() {
        UserEntity user = UserEntity.builder()
                .email("user@example.com")
                .name("tester")
                .password("encoded-password")
                .profile("profile.png")
                .build();
        user.setId(1);
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
}
