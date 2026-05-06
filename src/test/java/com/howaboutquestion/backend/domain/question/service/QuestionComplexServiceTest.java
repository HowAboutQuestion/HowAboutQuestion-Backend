package com.howaboutquestion.backend.domain.question.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.entity.Visibility;
import com.howaboutquestion.backend.domain.book.service.BookComplexService;
import com.howaboutquestion.backend.domain.question.dto.mapper.QuestionMapper;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionCreateRequest;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionUpdateRequest;
import com.howaboutquestion.backend.domain.question.dto.response.QuestionResponse;
import com.howaboutquestion.backend.domain.question.entity.*;
import com.howaboutquestion.backend.domain.tag.service.TagService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuestionComplexServiceTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private BookComplexService bookComplexService;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private TagService tagService;

    @InjectMocks
    private QuestionComplexService questionComplexService;

    @DisplayName("객관식 문제 생성에 성공한다")
    @Test
    void createMultipleQuestionReturnsMappedResponse() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionEntity question = createMultipleQuestion(book);
        QuestionCreateRequest request = QuestionCreateRequest.builder()
                .bookId(10)
                .title("객관식 문제")
                .description("설명")
                .picture("image.png")
                .level(Level.TWO)
                .type(QuestionType.MULTIPLE)
                .selectOne("1번")
                .multipleAnswer(MultipleAnswer.ONE)
                .build();
        QuestionResponse response = createQuestionResponse();

        given(bookComplexService.findOwnedBook(1L, 10)).willReturn(book);
        given(questionService.createQuestion(book, request)).willReturn(question);
        given(questionMapper.mapToQuestionResponse(question)).willReturn(response);

        QuestionResponse result = questionComplexService.createQuestion(1L, request);

        assertThat(result).isSameAs(response);
    }

    @DisplayName("문제 목록 조회에 성공한다")
    @Test
    void getQuestionsReturnsMappedResponses() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionEntity question = createMultipleQuestion(book);
        QuestionResponse response = createQuestionResponse();

        given(bookComplexService.findOwnedBook(1L, 10)).willReturn(book);
        given(questionService.getQuestions(10)).willReturn(List.of(question));
        given(questionMapper.mapToQuestionResponse(question)).willReturn(response);

        List<QuestionResponse> results = questionComplexService.getQuestions(1L, 10);

        assertThat(results).containsExactly(response);
    }

    @DisplayName("문제 상세 조회에 성공한다")
    @Test
    void getQuestionDetailReturnsMappedResponse() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionEntity question = createMultipleQuestion(book);
        QuestionResponse response = createQuestionResponse();

        given(questionService.findQuestionById(100)).willReturn(question);
        given(questionMapper.mapToQuestionResponse(question)).willReturn(response);

        QuestionResponse result = questionComplexService.getQuestionDetail(1L, 100);

        assertThat(result).isSameAs(response);
    }

    @DisplayName("타인의 문제 상세 조회는 권한 예외를 반환한다")
    @Test
    void getQuestionDetailThrowsWhenNotOwner() {
        UserEntity otherUser = createUserEntity(2);
        BookEntity otherBook = createBookEntity(otherUser);
        QuestionEntity question = createMultipleQuestion(otherBook);

        given(questionService.findQuestionById(100)).willReturn(question);

        assertThatThrownBy(() -> questionComplexService.getQuestionDetail(1L, 100))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.NO_USER_PERMISSION);
    }

    @DisplayName("문제 수정에 성공한다")
    @Test
    void updateQuestionReturnsMappedResponse() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionEntity question = createMultipleQuestion(book);
        QuestionUpdateRequest request = QuestionUpdateRequest.builder()
                .title("수정 문제")
                .description("수정 설명")
                .picture("new.png")
                .level(Level.THREE)
                .type(QuestionType.MULTIPLE)
                .selectOne("새 1번")
                .multipleAnswer(MultipleAnswer.TWO)
                .build();
        QuestionResponse response = createQuestionResponse();

        given(questionService.findQuestionById(100)).willReturn(question);
        given(questionMapper.mapToQuestionResponse(question)).willReturn(response);

        QuestionResponse result = questionComplexService.updateQuestion(1L, 100, request);

        verify(questionService).updateQuestion(question, request);
        assertThat(result).isSameAs(response);
    }

    @DisplayName("문제 삭제에 성공한다")
    @Test
    void deleteQuestionDeletesOwnedQuestion() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionEntity question = createMultipleQuestion(book);

        given(questionService.findQuestionById(100)).willReturn(question);

        questionComplexService.deleteQuestion(1L, 100);

        verify(questionService).deleteQuestion(question);
    }

    @DisplayName("문제 검색에 성공한다")
    @Test
    void searchQuestionsReturnsMappedResponses() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionEntity question = createMultipleQuestion(book);
        QuestionResponse response = createQuestionResponse();

        given(questionService.searchQuestions(1L, "객관식")).willReturn(List.of(question));
        given(questionMapper.mapToQuestionResponse(question)).willReturn(response);

        List<QuestionResponse> results = questionComplexService.searchQuestions(1L, "객관식", null);

        assertThat(results).containsExactly(response);
    }

    @DisplayName("문제집 범위 자동완성에 성공한다")
    @Test
    void autocompleteTitlesReturnsLimitedTitles() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);

        given(bookComplexService.findOwnedBook(1L, 10)).willReturn(book);
        given(questionService.autocompleteTitles(1L, 10, "객"))
                .willReturn(List.of("객관식 문제", "객체지향 문제"));

        List<String> results = questionComplexService.autocompleteTitles(1L, "객", 10);

        assertThat(results).containsExactly("객관식 문제", "객체지향 문제");
    }

    @DisplayName("태그 목록 조회에 성공한다")
    @Test
    void getTagNamesReturnsTagList() {
        given(tagService.getAllTagNames()).willReturn(List.of("자료구조", "알고리즘"));

        List<String> results = questionComplexService.getTagNames();

        assertThat(results).containsExactly("자료구조", "알고리즘");
    }

    @DisplayName("태그 기반 문제 필터링에 성공한다")
    @Test
    void getQuestionsByTagReturnsMappedResponses() {
        UserEntity user = createUserEntity(1);
        BookEntity book = createBookEntity(user);
        QuestionEntity question = createMultipleQuestion(book);
        QuestionResponse response = createQuestionResponse();

        given(questionService.getQuestionsByTag(1L, "자료구조")).willReturn(List.of(question));
        given(questionMapper.mapToQuestionResponse(question)).willReturn(response);

        List<QuestionResponse> results = questionComplexService.getQuestionsByTag(1L, "자료구조", null);

        assertThat(results).containsExactly(response);
    }

    @DisplayName("빈 검색어로 검색하면 예외를 반환한다")
    @Test
    void searchQuestionsThrowsWhenKeywordBlank() {
        assertThatThrownBy(() -> questionComplexService.searchQuestions(1L, " ", null))
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

    private QuestionEntity createMultipleQuestion(BookEntity book) {
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
                .answer(MultipleAnswer.ONE)
                .build();
    }

    private QuestionResponse createQuestionResponse() {
        return QuestionResponse.builder()
                .id(100)
                .bookId(10)
                .title("객관식 문제")
                .description("설명")
                .picture("image.png")
                .level(Level.TWO)
                .type(QuestionType.MULTIPLE)
                .selectOne("1번")
                .selectTwo("2번")
                .multipleAnswer(MultipleAnswer.ONE)
                .createdAt(LocalDateTime.of(2026, 5, 6, 0, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 6, 1, 0))
                .build();
    }
}
