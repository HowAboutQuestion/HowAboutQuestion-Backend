package com.howaboutquestion.backend.domain.exam.repository;

import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.repository<br>
 * fileName       : ExamRepository.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : Exam entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 * 26.05.06          eunchang          히스토리 목록 조회 쿼리 추가<br>
 */
@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Integer> {

    List<ExamEntity> findAllByBookUserIdOrderByCreatedAtDesc(Integer userId);
}
