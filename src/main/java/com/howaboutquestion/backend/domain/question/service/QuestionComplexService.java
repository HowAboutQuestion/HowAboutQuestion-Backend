package com.howaboutquestion.backend.domain.question.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.book.service.BookComplexService;
import com.howaboutquestion.backend.domain.question.dto.mapper.QuestionMapper;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionCreateRequest;
import com.howaboutquestion.backend.domain.question.dto.request.QuestionUpdateRequest;
import com.howaboutquestion.backend.domain.question.dto.response.QuestionResponse;
import com.howaboutquestion.backend.domain.question.entity.QuestionEntity;
import com.howaboutquestion.backend.domain.tag.service.TagService;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.service<br>
 * fileName       : QuestionComplexService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 문제 유스케이스를 조합하는 ComplexService 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 * 26.05.06          eunchang          검색/자동완성/태그 조회 기능 추가<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionComplexService {

    private final QuestionService questionService;
    private final BookComplexService bookComplexService;
    private final QuestionMapper questionMapper;
    private final TagService tagService;

    public QuestionResponse createQuestion(Long userId, QuestionCreateRequest request) {
        BookEntity book = bookComplexService.findOwnedBook(userId, request.getBookId());
        QuestionEntity question = questionService.createQuestion(book, request);
        return questionMapper.mapToQuestionResponse(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestions(Long userId, Integer bookId) {
        bookComplexService.findOwnedBook(userId, bookId);
        return questionService.getQuestions(bookId)
                .stream()
                .map(questionMapper::mapToQuestionResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionDetail(Long userId, Integer questionId) {
        return questionMapper.mapToQuestionResponse(findOwnedQuestion(userId, questionId));
    }

    public QuestionResponse updateQuestion(Long userId, Integer questionId, QuestionUpdateRequest request) {
        QuestionEntity question = findOwnedQuestion(userId, questionId);
        questionService.updateQuestion(question, request);
        return questionMapper.mapToQuestionResponse(question);
    }

    public void deleteQuestion(Long userId, Integer questionId) {
        QuestionEntity question = findOwnedQuestion(userId, questionId);
        questionService.deleteQuestion(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> searchQuestions(Long userId, String keyword, Integer bookId) {
        validateKeyword(keyword);

        if (bookId != null) {
            bookComplexService.findOwnedBook(userId, bookId);
            return questionService.searchQuestions(userId, bookId, keyword)
                    .stream()
                    .map(questionMapper::mapToQuestionResponse)
                    .toList();
        }

        return questionService.searchQuestions(userId, keyword)
                .stream()
                .map(questionMapper::mapToQuestionResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> autocompleteTitles(Long userId, String keyword, Integer bookId) {
        validateKeyword(keyword);

        if (bookId != null) {
            bookComplexService.findOwnedBook(userId, bookId);
            return questionService.autocompleteTitles(userId, bookId, keyword).stream()
                    .limit(10)
                    .toList();
        }

        return questionService.autocompleteTitles(userId, keyword).stream()
                .limit(10)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getTagNames() {
        return tagService.getAllTagNames();
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByTag(Long userId, String tagName, Integer bookId) {
        validateKeyword(tagName);

        if (bookId != null) {
            bookComplexService.findOwnedBook(userId, bookId);
            return questionService.getQuestionsByTag(userId, bookId, tagName)
                    .stream()
                    .map(questionMapper::mapToQuestionResponse)
                    .toList();
        }

        return questionService.getQuestionsByTag(userId, tagName)
                .stream()
                .map(questionMapper::mapToQuestionResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionEntity findOwnedQuestion(Long userId, Integer questionId) {
        QuestionEntity question = questionService.findQuestionById(questionId);

        if (!question.getBook().getUser().getId().equals(Math.toIntExact(userId))) {
            throw new CustomException(StatusCode.NO_USER_PERMISSION);
        }

        return question;
    }

    private void validateKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new CustomException(StatusCode.INVALID_PARAMETER);
        }
    }
}
