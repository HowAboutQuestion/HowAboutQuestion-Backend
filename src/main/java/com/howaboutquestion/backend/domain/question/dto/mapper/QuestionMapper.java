package com.howaboutquestion.backend.domain.question.dto.mapper;

import com.howaboutquestion.backend.domain.question.dto.response.QuestionResponse;
import com.howaboutquestion.backend.domain.question.entity.QuestionEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionMultipleEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionSubjectiveEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * packageName    : com.howaboutquestion.backend.domain.question.dto.mapper<br>
 * fileName       : QuestionMapper.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : Question entity 의 mapper 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {

    default QuestionResponse mapToQuestionResponse(QuestionEntity question) {
        QuestionResponse.QuestionResponseBuilder builder = QuestionResponse.builder()
                .id(question.getId())
                .bookId(question.getBook().getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .picture(question.getPicture())
                .level(question.getLevel())
                .type(question.getType())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt());

        if (question instanceof QuestionMultipleEntity multipleQuestion) {
            builder.selectOne(multipleQuestion.getSelectOne())
                    .selectTwo(multipleQuestion.getSelectTwo())
                    .selectThree(multipleQuestion.getSelectThree())
                    .selectFour(multipleQuestion.getSelectFour())
                    .selectFive(multipleQuestion.getSelectFive())
                    .multipleAnswer(multipleQuestion.getAnswer());
        }

        if (question instanceof QuestionSubjectiveEntity subjectiveQuestion) {
            builder.subjectiveAnswer(subjectiveQuestion.getAnswer());
        }

        return builder.build();
    }
}
