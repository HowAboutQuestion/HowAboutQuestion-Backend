package com.howaboutquestion.backend.domain.exam.dto.mapper;

import com.howaboutquestion.backend.domain.exam.dto.response.ExamStartQuestionResponse;
import com.howaboutquestion.backend.domain.question.entity.QuestionEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionMultipleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * packageName    : com.howaboutquestion.backend.domain.exam.dto.mapper<br>
 * fileName       : ExamMapper.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 시작 응답용 mapper 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExamMapper {

    default ExamStartQuestionResponse mapToExamStartQuestionResponse(QuestionEntity question) {
        ExamStartQuestionResponse.ExamStartQuestionResponseBuilder builder = ExamStartQuestionResponse.builder()
                .questionId(question.getId())
                .type(question.getType())
                .title(question.getTitle())
                .description(question.getDescription())
                .picture(question.getPicture());

        if (question instanceof QuestionMultipleEntity multipleQuestion) {
            builder.selectOne(multipleQuestion.getSelectOne())
                    .selectTwo(multipleQuestion.getSelectTwo())
                    .selectThree(multipleQuestion.getSelectThree())
                    .selectFour(multipleQuestion.getSelectFour())
                    .selectFive(multipleQuestion.getSelectFive());
        }

        return builder.build();
    }
}
