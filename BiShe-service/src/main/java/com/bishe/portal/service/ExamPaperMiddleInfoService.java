package com.bishe.portal.service;

import com.bishe.portal.model.vo.*;

import java.util.List;

public interface ExamPaperMiddleInfoService {
    void addQuestionInExamPaper(AddExamPaperMiddleVo addExamPaperMiddleVo);

    ExamPaperMiddleInfoVo getExamPaperInfo(String examPaperNum);

    void removeExamPaperSubject(String examPaperNum, List<SimpleSubjectVo> subjectId);

    void addParamSelect(ExamSelectInfoVo selectInfoVo);

    void addParamJudge(ExamJudgeInfoVo judgeInfoVo);


    PageShowVo getAllSelectExamPaperSubject(PageShowVo pageShowVo);

    PageShowVo getAllJudgeExamPaperSubject(PageShowVo pageShowVo);
}
