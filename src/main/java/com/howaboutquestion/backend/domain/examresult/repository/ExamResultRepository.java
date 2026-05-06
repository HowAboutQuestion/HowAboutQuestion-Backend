package com.howaboutquestion.backend.domain.examresult.repository;

import com.howaboutquestion.backend.domain.examresult.entity.ExamResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.examresult.repository<br>
 * fileName       : ExamResultRepository.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : ExamResult entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Repository
public interface ExamResultRepository extends JpaRepository<ExamResultEntity, Integer> {

    List<ExamResultEntity> findAllByExamIdOrderByIdAsc(Integer examId);
    boolean existsByExamId(Integer examId);
}
