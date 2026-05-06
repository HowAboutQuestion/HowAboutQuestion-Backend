package com.howaboutquestion.backend.domain.exam.service;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.exam.repository.ExamRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.service<br>
 * fileName       : ExamService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 도메인 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 * 26.05.06          eunchang          시험 조회 및 결과 갱신 메서드 추가<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    public ExamEntity startExam(DailyHistoryEntity dailyHistory, BookEntity book) {
        return examRepository.save(ExamEntity.builder()
                .dailyHistory(dailyHistory)
                .correctQuestion(0)
                .solvedQuestion(0)
                .rate(BigDecimal.ZERO)
                .tag(null)
                .book(book)
                .build());
    }

    @Transactional(readOnly = true)
    public ExamEntity findExamById(Integer examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND));
    }

    public void updateExamResult(ExamEntity exam, Integer correctQuestion, Integer solvedQuestion, BigDecimal rate) {
        exam.updateResult(correctQuestion, solvedQuestion, rate);
    }
}
