package com.howaboutquestion.backend.domain.book.repository;

import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.book.repository<br>
 * fileName       : BookRepository.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : Book entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findAllByUserIdOrderByCreatedAtDesc(Integer userId);
}
