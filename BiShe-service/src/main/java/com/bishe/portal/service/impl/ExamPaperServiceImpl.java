package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbExamPaperDao;
import com.bishe.portal.model.mo.TbExamPaper;
import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.service.ExamPaperService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 73515
 */
@Service
public class ExamPaperServiceImpl implements ExamPaperService {
    @Resource
    TbExamPaperDao tbExamPaperDao;
    @Override
    public void addExamPaper(ExamPaperVo examPaperVo) {
        tbExamPaperDao.insertExamPaper(getExamPaper(examPaperVo));
    }

    @Override
    public void updateExamPaper(ExamPaperVo examPaperVo) {
        tbExamPaperDao.updateExamPaper(getExamPaper(examPaperVo));
    }

    @Override
    public void deleteExamPaper(int examPaperId) {
        tbExamPaperDao.deleteExamPaper(examPaperId);
    }

    private TbExamPaper getExamPaper(ExamPaperVo examPaperVo) {
        TbExamPaper result  = new TbExamPaper();
        result.setCreateUnit(examPaperVo.getCreateUnit());
        result.setExamPaperName(examPaperVo.getExamPaperName());
        result.setExamPaperType(examPaperVo.getExamPaperType());
        result.setInvalidTime(examPaperVo.getInvalidTime());
        result.setResponsible(examPaperVo.getResponsible());
        result.setStatus(examPaperVo.getStatus());
        return result;
    }
}
