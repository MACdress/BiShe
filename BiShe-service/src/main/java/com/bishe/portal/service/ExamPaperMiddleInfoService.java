package com.bishe.portal.service;

import com.bishe.portal.model.vo.ExamJudgeInfoVo;
import com.bishe.portal.model.vo.ExamPaperMiddleInfoVo;
import com.bishe.portal.model.vo.ExamSelectInfoVo;

import java.util.List;

public interface ExamPaperMiddleInfoService {
    void addQuestionInExamPaper(List<ExamSelectInfoVo> examSelectList, List<ExamJudgeInfoVo> judgeInfoList);

    ExamPaperMiddleInfoVo getExamPaperInfo(String examPaperNum);

    void removeExamPaperSubject(String examPaperNum, String subjectId);
}
