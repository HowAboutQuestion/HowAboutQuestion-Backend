package com.howaboutquestion.backend.domain.history.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.entity.Visibility;
import com.howaboutquestion.backend.domain.exam.dto.mapper.ExamMapper;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamResultItemResponse;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamResultResponse;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.examresult.entity.ExamMultiple;
import com.howaboutquestion.backend.domain.examresult.entity.ExamResultEntity;
import com.howaboutquestion.backend.domain.examresult.service.ExamResultService;
import com.howaboutquestion.backend.domain.history.dto.response.ExamHistorySummaryResponse;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class HistoryComplexServiceTest {

    @Mock
    private HistoryService historyService;

    @Mock
    private ExamResultService examResultService;

    @Mock
    private ExamMapper examMapper;

    @InjectMocks
    private HistoryComplexService historyComplexService;

    @DisplayName("시험 히스토리 목록 조회에 성공한다")
    @Test
    void getExamHistoriesReturnsSummaryList() {
        UserEntity user = createUserEntity(1);
        ExamEntity exam = createExamEntity(user, 100);

        given(historyService.getExamHistories(1L)).willReturn(List.of(exam));

        List<ExamHistorySummaryResponse> results = historyComplexService.getExamHistories(1L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getExamId()).isEqualTo(100);
        assertThat(results.get(0).getBookTitle()).isEqualTo("자료구조");
    }

    @DisplayName("시험 히스토리 상세 조회에 성공한다")
    @Test
    void getExamHistoryDetailReturnsResultResponse() {
        UserEntity user = createUserEntity(1);
        ExamEntity exam = createExamEntity(user, 100);
        ExamResultEntity resultEntity = createExamResultEntity(exam);
        ExamResultItemResponse resultItemResponse = createExamResultItemResponse();

        given(historyService.findExamById(100)).willReturn(exam);
        given(examResultService.getExamResults(100)).willReturn(List.of(resultEntity));
        given(examMapper.mapToExamResultItemResponse(resultEntity)).willReturn(resultItemResponse);

        ExamResultResponse response = historyComplexService.getExamHistoryDetail(1L, 100);

        assertThat(response.getExamId()).isEqualTo(100);
        assertThat(response.getResults()).containsExactly(resultItemResponse);
    }

    @DisplayName("타인의 시험 히스토리 상세 조회는 권한 예외를 반환한다")
    @Test
    void getExamHistoryDetailThrowsWhenNotOwner() {
        UserEntity otherUser = createUserEntity(2);
        ExamEntity exam = createExamEntity(otherUser, 100);

        given(historyService.findExamById(100)).willReturn(exam);

        assertThatThrownBy(() -> historyComplexService.getExamHistoryDetail(1L, 100))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(StatusCode.NO_USER_PERMISSION);
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

    private ExamEntity createExamEntity(UserEntity user, Integer examId) {
        BookEntity book = BookEntity.builder()
                .id(10)
                .user(user)
                .title("자료구조")
                .content("기본 정리")
                .visibility(Visibility.PRIVATE)
                .checkFavorite(false)
                .createdAt(LocalDateTime.of(2026, 5, 6, 0, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 6, 1, 0))
                .build();

        return ExamEntity.builder()
                .id(examId)
                .createdAt(LocalDateTime.of(2026, 5, 6, 10, 0))
                .book(book)
                .correctQuestion(1)
                .solvedQuestion(2)
                .rate(BigDecimal.valueOf(50.00))
                .build();
    }

    private ExamResultEntity createExamResultEntity(ExamEntity exam) {
        return ExamMultiple.builder()
                .id(200)
                .type(com.howaboutquestion.backend.domain.question.entity.QuestionType.MULTIPLE)
                .title("객관식 문제")
                .description("설명")
                .picture("image.png")
                .exam(exam)
                .checkCorrect(true)
                .tag(null)
                .selectOne("1번")
                .selectTwo("2번")
                .selectThree("3번")
                .selectFour("4번")
                .selectFive("5번")
                .answer(com.howaboutquestion.backend.domain.question.entity.MultipleAnswer.ONE)
                .build();
    }

    private ExamResultItemResponse createExamResultItemResponse() {
        return ExamResultItemResponse.builder()
                .examResultId(200)
                .type(com.howaboutquestion.backend.domain.question.entity.QuestionType.MULTIPLE)
                .title("객관식 문제")
                .description("설명")
                .picture("image.png")
                .checkCorrect(true)
                .selectOne("1번")
                .selectTwo("2번")
                .selectThree("3번")
                .selectFour("4번")
                .selectFive("5번")
                .multipleAnswer(com.howaboutquestion.backend.domain.question.entity.MultipleAnswer.ONE)
                .build();
    }
}
