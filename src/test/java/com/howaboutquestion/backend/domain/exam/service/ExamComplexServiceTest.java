package com.howaboutquestion.backend.domain.exam.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.entity.Visibility;
import com.howaboutquestion.backend.domain.book.service.BookComplexService;
import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import com.howaboutquestion.backend.domain.dailyhistory.service.DailyHistoryService;
import com.howaboutquestion.backend.domain.exam.dto.mapper.ExamMapper;
import com.howaboutquestion.backend.domain.exam.dto.request.ExamStartRequest;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamStartQuestionResponse;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamStartResponse;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.question.entity.Level;
import com.howaboutquestion.backend.domain.question.entity.MultipleAnswer;
import com.howaboutquestion.backend.domain.question.entity.QuestionEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionMultipleEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionType;
import com.howaboutquestion.backend.domain.question.service.QuestionService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ExamComplexServiceTest {

    @Mock
    private ExamService examService;

    @Mock
    private DailyHistoryService dailyHistoryService;

    @Mock
    private BookComplexService bookComplexService;

    @Mock
    private QuestionService questionService;

    @Mock
    private UserService userService;

    @Mock
    private ExamMapper examMapper;

    @InjectMocks
    private ExamComplexService examComplexService;

    @DisplayName("시험 시작에 성공한다")
    @Test
    void startExamReturnsExamStartResponse() {
        UserEntity user = createUserEntity();
        BookEntity book = createBookEntity(user);
        DailyHistoryEntity dailyHistory = createDailyHistory(user);
        QuestionEntity question = createQuestion(book);
        ExamEntity exam = createExamEntity(book, dailyHistory);
        ExamStartQuestionResponse questionResponse = createExamStartQuestionResponse();
        ExamStartRequest request = ExamStartRequest.builder().bookId(10).build();

        given(userService.findUserEntityById(1L)).willReturn(user);
        given(bookComplexService.findOwnedBook(1L, 10)).willReturn(book);
        given(questionService.getQuestions(10)).willReturn(List.of(question));
        given(dailyHistoryService.getOrCreateTodayHistory(user)).willReturn(dailyHistory);
        given(examService.startExam(dailyHistory, book)).willReturn(exam);
        given(examMapper.mapToExamStartQuestionResponse(question)).willReturn(questionResponse);

        ExamStartResponse response = examComplexService.startExam(1L, request);

        assertThat(response.getExamId()).isEqualTo(100);
        assertThat(response.getBookId()).isEqualTo(10);
        assertThat(response.getBookTitle()).isEqualTo("자료구조");
        assertThat(response.getQuestions()).containsExactly(questionResponse);
    }

    @DisplayName("문제가 없는 문제집으로 시험 시작하면 예외를 반환한다")
    @Test
    void startExamThrowsWhenQuestionListEmpty() {
        UserEntity user = createUserEntity();
        BookEntity book = createBookEntity(user);
        ExamStartRequest request = ExamStartRequest.builder().bookId(10).build();

        given(userService.findUserEntityById(1L)).willReturn(user);
        given(bookComplexService.findOwnedBook(1L, 10)).willReturn(book);
        given(questionService.getQuestions(10)).willReturn(List.of());

        assertThatThrownBy(() -> examComplexService.startExam(1L, request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.INVALID_PARAMETER);
    }

    @DisplayName("타인의 문제집으로 시험 시작하면 권한 예외를 그대로 반환한다")
    @Test
    void startExamThrowsWhenBookNotOwned() {
        UserEntity user = createUserEntity();
        ExamStartRequest request = ExamStartRequest.builder().bookId(10).build();

        given(userService.findUserEntityById(1L)).willReturn(user);
        given(bookComplexService.findOwnedBook(1L, 10))
                .willThrow(new CustomException(StatusCode.NO_USER_PERMISSION));

        assertThatThrownBy(() -> examComplexService.startExam(1L, request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.NO_USER_PERMISSION);
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

    private DailyHistoryEntity createDailyHistory(UserEntity user) {
        return DailyHistoryEntity.builder()
                .id(20)
                .user(user)
                .date(LocalDate.of(2026, 5, 6))
                .correctQuestion(0)
                .solvedQuestion(0)
                .rate(BigDecimal.ZERO)
                .build();
    }

    private QuestionEntity createQuestion(BookEntity book) {
        return QuestionMultipleEntity.builder()
                .id(30)
                .book(book)
                .title("객관식 문제")
                .description("설명")
                .picture("image.png")
                .level(Level.ONE)
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

    private ExamEntity createExamEntity(BookEntity book, DailyHistoryEntity dailyHistory) {
        return ExamEntity.builder()
                .id(100)
                .createdAt(LocalDateTime.of(2026, 5, 6, 10, 0))
                .dailyHistory(dailyHistory)
                .correctQuestion(0)
                .solvedQuestion(0)
                .rate(BigDecimal.ZERO)
                .tag(null)
                .book(book)
                .build();
    }

    private ExamStartQuestionResponse createExamStartQuestionResponse() {
        return ExamStartQuestionResponse.builder()
                .questionId(30)
                .type(QuestionType.MULTIPLE)
                .title("객관식 문제")
                .description("설명")
                .picture("image.png")
                .selectOne("1번")
                .selectTwo("2번")
                .selectThree("3번")
                .selectFour("4번")
                .selectFive("5번")
                .build();
    }
}
