package com.howaboutquestion.backend.domain.dailyhistory.service;

import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import com.howaboutquestion.backend.domain.dailyhistory.repository.DailyHistoryRepository;
import com.howaboutquestion.backend.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * packageName    : com.howaboutquestion.backend.domain.dailyhistory.service<br>
 * fileName       : DailyHistoryService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 일별 히스토리 도메인 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 * 26.05.06          eunchang          시험 결과 반영 메서드 추가<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DailyHistoryService {

    private final DailyHistoryRepository dailyHistoryRepository;

    public DailyHistoryEntity getOrCreateTodayHistory(UserEntity user) {
        return dailyHistoryRepository.findByUserIdAndDate(user.getId(), LocalDate.now())
                .orElseGet(() -> dailyHistoryRepository.save(DailyHistoryEntity.builder()
                        .user(user)
                        .date(LocalDate.now())
                        .correctQuestion(0)
                        .solvedQuestion(0)
                        .rate(BigDecimal.ZERO)
                        .build()));
    }

    public void applyExamResult(DailyHistoryEntity dailyHistory, Integer correctQuestion, Integer solvedQuestion, BigDecimal rate) {
        dailyHistory.applyExamResult(correctQuestion, solvedQuestion, rate);
    }
}
