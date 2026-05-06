package com.howaboutquestion.backend.domain.exam.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.service.BookComplexService;
import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import com.howaboutquestion.backend.domain.dailyhistory.service.DailyHistoryService;
import com.howaboutquestion.backend.domain.exam.dto.mapper.ExamMapper;
import com.howaboutquestion.backend.domain.exam.dto.request.ExamStartRequest;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamStartQuestionResponse;
import com.howaboutquestion.backend.domain.exam.dto.response.ExamStartResponse;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionEntity;
import com.howaboutquestion.backend.domain.question.service.QuestionService;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import com.howaboutquestion.backend.domain.user.service.UserService;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
