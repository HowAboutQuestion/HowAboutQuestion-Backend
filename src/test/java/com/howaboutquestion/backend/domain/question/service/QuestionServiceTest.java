package com.howaboutquestion.backend.domain.question.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.entity.Visibility;
import com.howaboutquestion.backend.domain.book.repository.BookRepository;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionCreateRequest;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionUpdateRequest;
import com.howaboutquestion.backend.domain.question.dto.response.QuestionResponse;
import com.howaboutquestion.backend.domain.question.entity.*;
import com.howaboutquestion.backend.domain.question.repository.QuestionRepository;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
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
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private QuestionService questionService;

    @DisplayName("객관식 문제 생성에 성공한다")
    @Test
    void createMultipleQuestionReturnsSavedQuestion() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionMultipleEntity question = createMultipleQuestion(book);
        QuestionCreateRequest request = QuestionCreateRequest.builder()
                .bookId(10)
                .title("객관식 문제")
                .description("설명")
                .picture("image.png")
                .level(Level.TWO)
                .type(QuestionType.MULTIPLE)
                .selectOne("1번")
                .selectTwo("2번")
                .multipleAnswer(MultipleAnswer.ONE)
                .build();

        given(bookRepository.findById(10)).willReturn(Optional.of(book));
        given(questionRepository.save(any(QuestionEntity.class))).willReturn(question);

        QuestionResponse response = questionService.createQuestion(1L, request);

        assertThat(response.getId()).isEqualTo(100);
        assertThat(response.getType()).isEqualTo(QuestionType.MULTIPLE);
        assertThat(response.getSelectOne()).isEqualTo("1번");
        assertThat(response.getMultipleAnswer()).isEqualTo(MultipleAnswer.ONE);
    }

    @DisplayName("주관식 문제 생성에 성공한다")
    @Test
    void createSubjectiveQuestionReturnsSavedQuestion() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionSubjectiveEntity question = createSubjectiveQuestion(book);
        QuestionCreateRequest request = QuestionCreateRequest.builder()
                .bookId(10)
                .title("주관식 문제")
                .description("설명")
                .level(Level.ONE)
                .type(QuestionType.SUBJECTIVE)
                .subjectiveAnswer("정답")
                .build();

        given(bookRepository.findById(10)).willReturn(Optional.of(book));
        given(questionRepository.save(any(QuestionEntity.class))).willReturn(question);

        QuestionResponse response = questionService.createQuestion(1L, request);

        assertThat(response.getType()).isEqualTo(QuestionType.SUBJECTIVE);
        assertThat(response.getSubjectiveAnswer()).isEqualTo("정답");
    }

    @DisplayName("문제 목록 조회에 성공한다")
    @Test
    void getQuestionsReturnsQuestionList() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        given(bookRepository.findById(10)).willReturn(Optional.of(book));
        given(questionRepository.findAllByBookIdOrderByCreatedAtDesc(10))
                .willReturn(List.of(createMultipleQuestion(book), createSubjectiveQuestion(book)));

        List<QuestionResponse> responses = questionService.getQuestions(1L, 10);

        assertThat(responses).hasSize(2);
    }

    @DisplayName("문제 상세 조회에 성공한다")
    @Test
    void getQuestionDetailReturnsQuestion() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        given(questionRepository.findById(100)).willReturn(Optional.of(createMultipleQuestion(book)));

        QuestionResponse response = questionService.getQuestionDetail(1L, 100);

        assertThat(response.getId()).isEqualTo(100);
        assertThat(response.getBookId()).isEqualTo(10);
    }

    @DisplayName("객관식 문제 수정에 성공한다")
    @Test
    void updateMultipleQuestionUpdatesFields() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionMultipleEntity question = createMultipleQuestion(book);
        QuestionUpdateRequest request = QuestionUpdateRequest.builder()
                .title("수정 객관식")
                .description("수정 설명")
                .picture("new.png")
                .level(Level.THREE)
                .type(QuestionType.MULTIPLE)
                .selectOne("새 1번")
                .selectTwo("새 2번")
                .multipleAnswer(MultipleAnswer.TWO)
                .build();

        given(questionRepository.findById(100)).willReturn(Optional.of(question));

        QuestionResponse response = questionService.updateQuestion(1L, 100, request);

        assertThat(response.getTitle()).isEqualTo("수정 객관식");
        assertThat(response.getMultipleAnswer()).isEqualTo(MultipleAnswer.TWO);
    }

    @DisplayName("타인의 문제를 수정하려고 하면 권한 예외를 반환한다")
    @Test
    void updateQuestionThrowsWhenNotOwner() {
        UserEntity otherUser = createUserEntity(2);
        BookEntity otherBook = createBookEntity(otherUser);
        QuestionUpdateRequest request = QuestionUpdateRequest.builder()
                .title("수정")
                .level(Level.ONE)
                .type(QuestionType.SUBJECTIVE)
                .subjectiveAnswer("정답")
                .build();

        given(questionRepository.findById(100)).willReturn(Optional.of(createSubjectiveQuestion(otherBook)));

        assertThatThrownBy(() -> questionService.updateQuestion(1L, 100, request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.NO_USER_PERMISSION);
    }

    @DisplayName("문제 삭제에 성공한다")
    @Test
    void deleteQuestionDeletesOwnedQuestion() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionEntity question = createMultipleQuestion(book);
        given(questionRepository.findById(100)).willReturn(Optional.of(question));

        questionService.deleteQuestion(1L, 100);

        verify(questionRepository).delete(question);
    }

    @DisplayName("객관식 필수 값이 없으면 생성을 거부한다")
    @Test
    void createQuestionThrowsWhenMultipleRequiredFieldMissing() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionCreateRequest request = QuestionCreateRequest.builder()
                .bookId(10)
                .title("객관식 문제")
                .level(Level.ONE)
                .type(QuestionType.MULTIPLE)
                .build();

        given(bookRepository.findById(10)).willReturn(Optional.of(book));

        assertThatThrownBy(() -> questionService.createQuestion(1L, request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.INVALID_PARAMETER);
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
                .title("문제집")
                .content("설명")
                .visibility(Visibility.PRIVATE)
                .checkFavorite(false)
                .createdAt(LocalDateTime.of(2026, 5, 6, 0, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 6, 1, 0))
                .build();
    }

    private QuestionMultipleEntity createMultipleQuestion(BookEntity book) {
        return QuestionMultipleEntity.builder()
                .id(100)
                .book(book)
                .title("객관식 문제")
                .description("설명")
                .picture("image.png")
                .level(Level.TWO)
                .type(QuestionType.MULTIPLE)
                .createdAt(LocalDateTime.of(2026, 5, 6, 0, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 6, 1, 0))
                .selectOne("1번")
                .selectTwo("2번")
                .selectThree("3번")
                .selectFour("4번")
                .selectFive("5번")
                .answer(MultipleAnswer.ONE)
                .build();
    }

    private QuestionSubjectiveEntity createSubjectiveQuestion(BookEntity book) {
        return QuestionSubjectiveEntity.builder()
                .id(101)
                .book(book)
                .title("주관식 문제")
                .description("설명")
                .level(Level.ONE)
                .type(QuestionType.SUBJECTIVE)
                .createdAt(LocalDateTime.of(2026, 5, 6, 0, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 6, 1, 0))
                .answer("정답")
                .build();
    }
}
