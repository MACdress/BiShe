package com.bishe.portal.service;

import com.bishe.portal.model.vo.ExamChooseVo;
import com.bishe.portal.model.vo.ExamPaperStartMiddleInfoVo;
import com.bishe.portal.model.vo.ExamStartVo;

import java.util.List;

public interface ExamStartService {
    ExamPaperStartMiddleInfoVo startExam(String examPaperNum, String account, Integer page, Integer pageSize, String examStartNum);

    ExamStartVo endExam(String examStartNum);

    void chooseExamSubject(ExamChooseVo examChooseVo);

    void chooseExamSubjectList(List<ExamChooseVo> examChooseList, String examStartNum);

    List<ExamStartVo> getAllExamHistory(int page, int pageSize, String examPaperNum);

    List<ExamStartVo> getUserExamHistory(Integer page, Integer pageSize, String account);
}
