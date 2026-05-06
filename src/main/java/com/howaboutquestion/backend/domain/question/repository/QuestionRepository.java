package com.howaboutquestion.backend.domain.question.repository;

import com.howaboutquestion.backend.domain.question.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.repository<br>
 * fileName       : QuestionRepository.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : Question entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {

    List<QuestionEntity> findAllByBookIdOrderByCreatedAtDesc(Integer bookId);
}
