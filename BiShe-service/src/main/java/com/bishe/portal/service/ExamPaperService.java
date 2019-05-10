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
    ExamPaperVo addExamPaper(ExamPaperVo examPaperVo);

    void updateExamPaper(ExamPaperVo examPaperVo);

    void deleteExamPaper(String examPaperNum);

    List<ExamPaperVo> findExamPaperList(FindExamPaperVo findExamPaperVo);

    List<ExamPaperVo> getReleaseExamPaper(String account);

    List<ExamPaperVo> findExamPaperListByType(Integer examPaperType,int page,int pageSize);
}
