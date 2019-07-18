package com.bishe.portal.service;

import com.bishe.portal.model.vo.*;

import java.util.List;

public interface ExamPaperMiddleInfoService {
    void addQuestionInExamPaper(AddExamPaperMiddleVo addExamPaperMiddleVo);

    void removeExamPaperSubject(String examPaperNum, List<SimpleSubjectVo> subjectId);

    ExamSelectInfoVo addParamSelect(ExamSelectInfoVo selectInfoVo);

    ExamJudgeInfoVo addParamJudge(ExamJudgeInfoVo judgeInfoVo);


    PageShowVo getAllSelectExamPaperSubject(PageShowVo pageShowVo);

    PageShowVo getAllJudgeExamPaperSubject(PageShowVo pageShowVo);

    ExamPaperMiddleInfoVo getExamPaperInfo(String examPaperNum, int page, int pageSize);
}
