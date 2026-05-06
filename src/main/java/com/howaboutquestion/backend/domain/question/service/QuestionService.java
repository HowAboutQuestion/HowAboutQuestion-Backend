package com.howaboutquestion.backend.domain.question.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.repository.BookRepository;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionCreateRequest;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionUpdateRequest;
import com.howaboutquestion.backend.domain.question.dto.response.QuestionResponse;
import com.howaboutquestion.backend.domain.question.entity.*;
import com.howaboutquestion.backend.domain.question.repository.QuestionRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.service<br>
 * fileName       : QuestionService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제 관련 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final BookRepository bookRepository;

    public QuestionResponse createQuestion(Long userId, QuestionCreateRequest request) {
        BookEntity book = findOwnedBook(userId, request.getBookId());
        validateRequestByType(request.getType(), request.getSelectOne(), request.getMultipleAnswer(), request.getSubjectiveAnswer());

        QuestionEntity question = switch (request.getType()) {
            case MULTIPLE -> QuestionMultipleEntity.builder()
                    .book(book)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .picture(request.getPicture())
                    .level(request.getLevel())
                    .type(QuestionType.MULTIPLE)
                    .selectOne(request.getSelectOne())
                    .selectTwo(request.getSelectTwo())
                    .selectThree(request.getSelectThree())
                    .selectFour(request.getSelectFour())
                    .selectFive(request.getSelectFive())
                    .answer(request.getMultipleAnswer())
                    .build();
            case SUBJECTIVE -> QuestionSubjectiveEntity.builder()
                    .book(book)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .picture(request.getPicture())
                    .level(request.getLevel())
                    .type(QuestionType.SUBJECTIVE)
                    .answer(request.getSubjectiveAnswer())
                    .build();
        };

        return toResponse(questionRepository.save(question));
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestions(Long userId, Integer bookId) {
        findOwnedBook(userId, bookId);
        return questionRepository.findAllByBookIdOrderByCreatedAtDesc(bookId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionDetail(Long userId, Integer questionId) {
        return toResponse(findOwnedQuestion(userId, questionId));
    }

    public QuestionResponse updateQuestion(Long userId, Integer questionId, QuestionUpdateRequest request) {
        QuestionEntity question = findOwnedQuestion(userId, questionId);
        validateRequestByType(request.getType(), request.getSelectOne(), request.getMultipleAnswer(), request.getSubjectiveAnswer());

        if (question instanceof QuestionMultipleEntity multipleQuestion && request.getType() == QuestionType.MULTIPLE) {
            multipleQuestion.updateQuestion(
                    request.getTitle(),
                    request.getDescription(),
                    request.getPicture(),
                    request.getLevel(),
                    request.getSelectOne(),
                    request.getSelectTwo(),
                    request.getSelectThree(),
                    request.getSelectFour(),
                    request.getSelectFive(),
                    request.getMultipleAnswer()
            );
            return toResponse(multipleQuestion);
        }

        if (question instanceof QuestionSubjectiveEntity subjectiveQuestion && request.getType() == QuestionType.SUBJECTIVE) {
            subjectiveQuestion.updateQuestion(
                    request.getTitle(),
                    request.getDescription(),
                    request.getPicture(),
                    request.getLevel(),
                    request.getSubjectiveAnswer()
            );
            return toResponse(subjectiveQuestion);
        }

        throw new CustomException(StatusCode.INVALID_PARAMETER);
    }

    public void deleteQuestion(Long userId, Integer questionId) {
        QuestionEntity question = findOwnedQuestion(userId, questionId);
        questionRepository.delete(question);
    }

    private BookEntity findOwnedBook(Long userId, Integer bookId) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND));

        if (!book.getUser().getId().equals(Math.toIntExact(userId))) {
            throw new CustomException(StatusCode.NO_USER_PERMISSION);
        }

        return book;
    }

    private QuestionEntity findOwnedQuestion(Long userId, Integer questionId) {
        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND));

        if (!question.getBook().getUser().getId().equals(Math.toIntExact(userId))) {
            throw new CustomException(StatusCode.NO_USER_PERMISSION);
        }

        return question;
    }

    private void validateRequestByType(
            QuestionType type,
            String selectOne,
            MultipleAnswer multipleAnswer,
            String subjectiveAnswer
    ) {
        if (type == QuestionType.MULTIPLE) {
            if (isBlank(selectOne) || multipleAnswer == null) {
                throw new CustomException(StatusCode.INVALID_PARAMETER);
            }
            return;
        }

        if (isBlank(subjectiveAnswer)) {
            throw new CustomException(StatusCode.INVALID_PARAMETER);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private QuestionResponse toResponse(QuestionEntity question) {
        QuestionResponse.QuestionResponseBuilder builder = QuestionResponse.builder()
                .id(question.getId())
                .bookId(question.getBook().getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .picture(question.getPicture())
                .level(question.getLevel())
                .type(question.getType())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt());

        if (question instanceof QuestionMultipleEntity multipleQuestion) {
            builder.selectOne(multipleQuestion.getSelectOne())
                    .selectTwo(multipleQuestion.getSelectTwo())
                    .selectThree(multipleQuestion.getSelectThree())
                    .selectFour(multipleQuestion.getSelectFour())
                    .selectFive(multipleQuestion.getSelectFive())
                    .multipleAnswer(multipleQuestion.getAnswer());
        }

        if (question instanceof QuestionSubjectiveEntity subjectiveQuestion) {
            builder.subjectiveAnswer(subjectiveQuestion.getAnswer());
        }

        return builder.build();
    }
}
