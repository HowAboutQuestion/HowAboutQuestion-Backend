package com.howaboutquestion.backend.domain.dashboard.service;

import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import com.howaboutquestion.backend.domain.dailyhistory.service.DailyHistoryService;
import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.exam.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.howaboutquestion.backend.domain.dashboard.service<br>
 * fileName       : DashboardService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 대시보드 조회 도메인 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final DailyHistoryService dailyHistoryService;
    private final ExamRepository examRepository;

    public Optional<DailyHistoryEntity> getTodayHistory(Long userId) {
        return dailyHistoryService.findTodayHistory(userId);
    }

    public List<ExamEntity> getRecentExams(Long userId) {
        return examRepository.findTop5ByBookUserIdOrderByCreatedAtDesc(Math.toIntExact(userId));
    }
}
