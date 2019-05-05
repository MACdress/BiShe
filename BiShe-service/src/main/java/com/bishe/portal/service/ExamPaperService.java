package com.bishe.portal.service;

import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.model.vo.FindExamPaperVo;

import java.util.List;

/**
 * @author 73515
 */
public interface ExamPaperService {
    /**
     *
     * @param examPaperVo
     */
    void addExamPaper(ExamPaperVo examPaperVo);

    void updateExamPaper(ExamPaperVo examPaperVo);

    void deleteExamPaper(String examPaperNum);

    List<ExamPaperVo> findExamPaperList(FindExamPaperVo findExamPaperVo);

    List<ExamPaperVo> getReleaseExamPaper();
}
