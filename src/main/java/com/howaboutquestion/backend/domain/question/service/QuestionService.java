package com.howaboutquestion.backend.domain.question.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionCreateRequest;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionUpdateRequest;
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
 * description    : 문제 도메인 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 * 26.05.06          eunchang          ComplexService 하위 도메인 서비스로 역할 정리<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionEntity createQuestion(BookEntity book, QuestionCreateRequest request) {
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

        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionEntity> getQuestions(Integer bookId) {
        return questionRepository.findAllByBookIdOrderByCreatedAtDesc(bookId);
    }

    @Transactional(readOnly = true)
    public QuestionEntity findQuestionById(Integer questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND));
    }

    public void updateQuestion(QuestionEntity question, QuestionUpdateRequest request) {
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
            return;
        }

        if (question instanceof QuestionSubjectiveEntity subjectiveQuestion && request.getType() == QuestionType.SUBJECTIVE) {
            subjectiveQuestion.updateQuestion(
                    request.getTitle(),
                    request.getDescription(),
                    request.getPicture(),
                    request.getLevel(),
                    request.getSubjectiveAnswer()
            );
            return;
        }

        throw new CustomException(StatusCode.INVALID_PARAMETER);
    }

    public void deleteQuestion(QuestionEntity question) {
        questionRepository.delete(question);
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

}
