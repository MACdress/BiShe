package com.bishe.portal.service;

import com.bishe.portal.model.vo.*;

import java.util.List;

public interface ExamPaperMiddleInfoService {
    void addQuestionInExamPaper(AddExamPaperMiddleVo addExamPaperMiddleVo);

    ExamPaperMiddleInfoVo getExamPaperInfo(String examPaperNum);

    void removeExamPaperSubject(String examPaperNum, List<SimpleSubjectVo> subjectId);

    ExamSelectInfoVo addParamSelect(ExamSelectInfoVo selectInfoVo);

    ExamJudgeInfoVo addParamJudge(ExamJudgeInfoVo judgeInfoVo);


    PageShowVo getAllSelectExamPaperSubject(PageShowVo pageShowVo);

    PageShowVo getAllJudgeExamPaperSubject(PageShowVo pageShowVo);
}
