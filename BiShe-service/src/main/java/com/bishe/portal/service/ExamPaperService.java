package com.bishe.portal.service;

import com.bishe.portal.model.vo.ExamPaperVo;

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

    void deleteExamPaper(int examPaperId);
}
