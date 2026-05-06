package com.howaboutquestion.backend.domain.exam.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.service.BookComplexService;
import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import com.howaboutquestion.backend.domain.dailyhistory.service.DailyHistoryService;
import com.howaboutquestion.backend.domain.exam.dto.mapper.ExamMapper;
import com.howaboutquestion.backend.domain.exam.dto.request.ExamStartRequest;
import com.howaboutquestion.backend.domain.exam.dto.request.ExamAnswerItemRequest;
import com.howaboutquestion.backend.domain.exam.dto.request.ExamAnswerSubmitRequest;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamResultItemResponse;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamResultResponse;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamStartQuestionResponse;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamStartResponse;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.examresult.entity.ExamResultEntity;
import com.howaboutquestion.backend.domain.examresult.service.ExamResultService;
import com.howaboutquestion.backend.domain.question.entity.MultipleAnswer;
import com.howaboutquestion.backend.domain.question.entity.QuestionEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionMultipleEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionSubjectiveEntity;
import com.howaboutquestion.backend.domain.question.service.QuestionService;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.service.UserService;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.service<br>
 * fileName       : ExamComplexService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 시작 유스케이스를 조합하는 ComplexService 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ExamComplexService {

    private final ExamService examService;
    private final DailyHistoryService dailyHistoryService;
    private final BookComplexService bookComplexService;
    private final QuestionService questionService;
    private final UserService userService;
    private final ExamMapper examMapper;
    private final ExamResultService examResultService;

    public ExamStartResponse startExam(Long userId, ExamStartRequest request) {
        UserEntity user = userService.findUserEntityById(userId);
        BookEntity book = bookComplexService.findOwnedBook(userId, request.getBookId());
        List<QuestionEntity> questions = questionService.getQuestions(book.getId());

        if (questions.isEmpty()) {
            throw new CustomException(StatusCode.INVALID_PARAMETER);
        }

        DailyHistoryEntity dailyHistory = dailyHistoryService.getOrCreateTodayHistory(user);
        ExamEntity exam = examService.startExam(dailyHistory, book);

        List<ExamStartQuestionResponse> questionResponses = questions.stream()
                .map(examMapper::mapToExamStartQuestionResponse)
                .toList();

        return ExamStartResponse.builder()
                .examId(exam.getId())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .startedAt(exam.getCreatedAt())
                .questions(questionResponses)
                .build();
    }

    public ExamResultResponse submitAnswers(Long userId, Integer examId, ExamAnswerSubmitRequest request) {
        ExamEntity exam = findOwnedExam(userId, examId);

        if (examResultService.hasSubmitted(examId)) {
            throw new CustomException(StatusCode.INVALID_PARAMETER);
        }

        List<QuestionEntity> questions = questionService.getQuestions(exam.getBook().getId());
        Map<Integer, QuestionEntity> questionMap = new HashMap<>();
        for (QuestionEntity question : questions) {
            questionMap.put(question.getId(), question);
        }

        int correctCount = 0;
        int solvedCount = 0;

        for (ExamAnswerItemRequest answerItem : request.getAnswers()) {
            QuestionEntity question = questionMap.get(answerItem.getQuestionId());
            if (question == null || question.getType() != answerItem.getType()) {
                throw new CustomException(StatusCode.INVALID_PARAMETER);
            }

            boolean checkCorrect = gradeAndSaveResult(exam, question, answerItem);
            solvedCount++;
            if (checkCorrect) {
                correctCount++;
            }
        }

        BigDecimal rate = calculateRate(correctCount, solvedCount);
        examService.updateExamResult(exam, correctCount, solvedCount, rate);
        dailyHistoryService.applyExamResult(exam.getDailyHistory(), correctCount, solvedCount, rate);

        return getExamResult(userId, examId);
    }

    @Transactional(readOnly = true)
    public ExamResultResponse getExamResult(Long userId, Integer examId) {
        ExamEntity exam = findOwnedExam(userId, examId);
        List<ExamResultItemResponse> resultItems = examResultService.getExamResults(examId).stream()
                .map(examMapper::mapToExamResultItemResponse)
                .toList();

        return ExamResultResponse.builder()
                .examId(exam.getId())
                .bookId(exam.getBook().getId())
                .bookTitle(exam.getBook().getTitle())
                .startedAt(exam.getCreatedAt())
                .correctQuestion(exam.getCorrectQuestion())
                .solvedQuestion(exam.getSolvedQuestion())
                .rate(exam.getRate())
                .results(resultItems)
                .build();
    }

    @Transactional(readOnly = true)
    public ExamEntity findOwnedExam(Long userId, Integer examId) {
        ExamEntity exam = examService.findExamById(examId);
        if (!exam.getBook().getUser().getId().equals(Math.toIntExact(userId))) {
            throw new CustomException(StatusCode.NO_USER_PERMISSION);
        }
        return exam;
    }

    private boolean gradeAndSaveResult(ExamEntity exam, QuestionEntity question, ExamAnswerItemRequest answerItem) {
        if (question instanceof QuestionMultipleEntity multipleQuestion) {
            MultipleAnswer userAnswer = answerItem.getMultipleAnswer();
            if (userAnswer == null) {
                throw new CustomException(StatusCode.INVALID_PARAMETER);
            }
            boolean correct = multipleQuestion.getAnswer() == userAnswer;
            examResultService.saveMultipleResult(exam, multipleQuestion, correct);
            return correct;
        }

        if (question instanceof QuestionSubjectiveEntity subjectiveQuestion) {
            String userAnswer = answerItem.getSubjectiveAnswer();
            if (userAnswer == null || userAnswer.isBlank()) {
                throw new CustomException(StatusCode.INVALID_PARAMETER);
            }
            boolean correct = subjectiveQuestion.getAnswer().trim().equals(userAnswer.trim());
            examResultService.saveSubjectiveResult(exam, subjectiveQuestion, correct);
            return correct;
        }

        throw new CustomException(StatusCode.INVALID_PARAMETER);
    }

    private BigDecimal calculateRate(int correctCount, int solvedCount) {
        if (solvedCount == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.valueOf(correctCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(solvedCount), 2, RoundingMode.HALF_UP);
    }
}
