package com.howaboutquestion.backend.domain.dailyhistory.repository;

import com.howaboutquestion.backend.domain.dailyhistory.entity.DailyHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * packageName    : com.howaboutquestion.backend.domain.dailyhistory.repository<br>
 * fileName       : DailyHistoryRepository.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : DailyHistory entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Repository
public interface DailyHistoryRepository extends JpaRepository<DailyHistoryEntity, Integer> {

    Optional<DailyHistoryEntity> findByUserIdAndDate(Integer userId, LocalDate date);
}
