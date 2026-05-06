package com.howaboutquestion.backend.domain.question.repository;

import com.howaboutquestion.backend.domain.question.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
 * 26.05.06          eunchang          검색/자동완성/태그 필터 쿼리 추가<br>
 */
@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {

    List<QuestionEntity> findAllByBookIdOrderByCreatedAtDesc(Integer bookId);

    @Query("""
            select distinct q
            from QuestionEntity q
            where q.book.user.id = :userId
              and (
                lower(q.title) like lower(concat('%', :keyword, '%'))
                or lower(coalesce(q.description, '')) like lower(concat('%', :keyword, '%'))
              )
            order by q.createdAt desc
            """)
    List<QuestionEntity> searchByUserId(Long userId, String keyword);

    @Query("""
            select distinct q
            from QuestionEntity q
            where q.book.user.id = :userId
              and q.book.id = :bookId
              and (
                lower(q.title) like lower(concat('%', :keyword, '%'))
                or lower(coalesce(q.description, '')) like lower(concat('%', :keyword, '%'))
              )
            order by q.createdAt desc
            """)
    List<QuestionEntity> searchByUserIdAndBookId(Long userId, Integer bookId, String keyword);

    @Query("""
            select distinct q.title
            from QuestionEntity q
            where q.book.user.id = :userId
              and lower(q.title) like lower(concat(:keyword, '%'))
            order by q.title asc
            """)
    List<String> autocompleteTitlesByUserId(Long userId, String keyword);

    @Query("""
            select distinct q.title
            from QuestionEntity q
            where q.book.user.id = :userId
              and q.book.id = :bookId
              and lower(q.title) like lower(concat(:keyword, '%'))
            order by q.title asc
            """)
    List<String> autocompleteTitlesByUserIdAndBookId(Long userId, Integer bookId, String keyword);

    @Query("""
            select distinct q
            from QuestionEntity q
            join q.tags qt
            join qt.tag t
            where q.book.user.id = :userId
              and lower(t.name) = lower(:tagName)
            order by q.createdAt desc
            """)
    List<QuestionEntity> findAllByUserIdAndTagName(Long userId, String tagName);

    @Query("""
            select distinct q
            from QuestionEntity q
            join q.tags qt
            join qt.tag t
            where q.book.user.id = :userId
              and q.book.id = :bookId
              and lower(t.name) = lower(:tagName)
            order by q.createdAt desc
            """)
    List<QuestionEntity> findAllByUserIdAndBookIdAndTagName(Long userId, Integer bookId, String tagName);
}
