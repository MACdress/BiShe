package com.bishe.portal.service;

import com.bishe.portal.model.vo.ExamChooseVo;
import com.bishe.portal.model.vo.ExamStartVo;

public interface ExamStartService {
    ExamStartVo startExam(String examPaperNum, String account);

    ExamStartVo endExam(String examStartNum);

    void chooseExamSubject(ExamChooseVo examChooseVo);
}
