package com.howaboutquestion.backend.domain.tag.service;

import com.howaboutquestion.backend.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.tag.service<br>
 * fileName       : TagService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 태그 관련 도메인 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<String> getAllTagNames() {
        return tagRepository.findAllTagNamesOrderByNameAsc();
    }
}
