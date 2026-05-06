package com.howaboutquestion.backend.domain.tag.repository;

import com.howaboutquestion.backend.domain.tag.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.tag.repository<br>
 * fileName       : TagRepository.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : Tag entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    @Query("select t.name from TagEntity t order by t.name asc")
    List<String> findAllTagNamesOrderByNameAsc();
}
