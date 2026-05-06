package com.howaboutquestion.backend.domain.examresult.service;

import com.howaboutquestion.backend.domain.exam.entity.ExamEntity;
import com.howaboutquestion.backend.domain.examresult.entity.ExamMultiple;
import com.howaboutquestion.backend.domain.examresult.entity.ExamResultEntity;
import com.howaboutquestion.backend.domain.examresult.entity.ExamSubjective;
import com.howaboutquestion.backend.domain.examresult.repository.ExamResultRepository;
import com.howaboutquestion.backend.domain.question.entity.QuestionMultipleEntity;
import com.howaboutquestion.backend.domain.question.entity.QuestionSubjectiveEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.howaboutquestion.backend.domain.examresult.service<br>
 * fileName       : ExamResultService.java<br>
 * author         : eunchang<br>
 * date           : 2026.05.06<br>
 * description    : 시험 결과 도메인 서비스 로직을 수행하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 26.05.06          eunchang          최초 생성<br>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ExamResultService {

    private final ExamResultRepository examResultRepository;

    public ExamResultEntity saveMultipleResult(
            ExamEntity exam,
            QuestionMultipleEntity question,
            boolean checkCorrect
    ) {
        return examResultRepository.save(ExamMultiple.builder()
                .type(question.getType())
                .title(question.getTitle())
                .description(question.getDescription())
                .picture(question.getPicture())
                .exam(exam)
                .checkCorrect(checkCorrect)
                .tag(null)
                .selectOne(question.getSelectOne())
                .selectTwo(question.getSelectTwo())
                .selectThree(question.getSelectThree())
                .selectFour(question.getSelectFour())
                .selectFive(question.getSelectFive())
                .answer(question.getAnswer())
                .build());
    }

    public ExamResultEntity saveSubjectiveResult(
            ExamEntity exam,
            QuestionSubjectiveEntity question,
            boolean checkCorrect
    ) {
        return examResultRepository.save(ExamSubjective.builder()
                .type(question.getType())
                .title(question.getTitle())
                .description(question.getDescription())
                .picture(question.getPicture())
                .exam(exam)
                .checkCorrect(checkCorrect)
                .tag(null)
                .answer(question.getAnswer())
                .build());
    }

    @Transactional(readOnly = true)
    public List<ExamResultEntity> getExamResults(Integer examId) {
        return examResultRepository.findAllByExamIdOrderByIdAsc(examId);
    }

    @Transactional(readOnly = true)
    public boolean hasSubmitted(Integer examId) {
        return examResultRepository.existsByExamId(examId);
    }
}
