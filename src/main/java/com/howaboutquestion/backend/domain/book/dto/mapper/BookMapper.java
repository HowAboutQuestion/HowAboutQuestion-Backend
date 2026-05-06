package com.howaboutquestion.backend.domain.book.dto.mapper;

import com.howaboutquestion.backend.domain.book.dto.response.BookResponse;
import com.howaboutquestion.backend.domain.book.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * packageName    : com.howaboutquestion.backend.domain.book.dto.mapper<br>
 * fileName       : BookMapper.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : Book entity 의 MapStruct 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(target = "userId", source = "user.id")
    BookResponse mapToBookResponse(BookEntity entity);
}
