package com.howaboutquestion.backend.domain.history.service;

import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.exam.repository.ExamRepository;
import com.howaboutquestion.backend.global.common.StatusCode;
import com.howaboutquestion.backend.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.history.service<br>
 * fileName       : HistoryService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 히스토리 조회 도메인 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryService {

    private final ExamRepository examRepository;

    public List<ExamEntity> getExamHistories(Long userId) {
        return examRepository.findAllByBookUserIdOrderByCreatedAtDesc(Math.toIntExact(userId));
    }

    public ExamEntity findExamById(Integer examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new CustomException(StatusCode.RESOURCE_NOT_FOUND));
    }
}
