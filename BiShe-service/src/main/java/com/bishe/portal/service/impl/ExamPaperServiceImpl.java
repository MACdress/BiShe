package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbExamPaperDao;
import com.bishe.portal.model.mo.TbExamPaper;
import com.bishe.portal.model.po.FindExamPaperPo;
import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.model.vo.FindExamPaperVo;
import com.bishe.portal.service.ExamPaperService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ExamPaperVo> findExamPaperList(FindExamPaperVo findExamPaperVo) {
        List<TbExamPaper> examPapers = tbExamPaperDao.selectExamPaper(getExamPaperPo(findExamPaperVo));
        List<ExamPaperVo> result = new ArrayList<>();
        for (TbExamPaper examPaper:examPapers){
            ExamPaperVo examPaperVo = getExamPaperVo(examPaper);
            result.add(examPaperVo);
        }
        return result;
    }

    private ExamPaperVo getExamPaperVo(TbExamPaper examPaper) {
        ExamPaperVo result = new ExamPaperVo();
        result.setCreateUnit(examPaper.getCreateUnit());
        result.setExamPaperName(examPaper.getExamPaperName());
        result.setInvalidTime(examPaper.getInvalidTime());
        result.setResponsible(examPaper.getResponsible());
        result.setStatus(examPaper.getStatus());
        result.setExamPaperType(examPaper.getExamPaperType());
        return result;
    }

    private FindExamPaperPo getExamPaperPo(FindExamPaperVo findExamPaperVo) {
        FindExamPaperPo result = new FindExamPaperPo();
        result.setCreateUnit(findExamPaperVo.getCreateUnit());
        result.setExamPaperName(findExamPaperVo.getExamPaperName());
        result.setInvalidBeginTime(findExamPaperVo.getInvalidBeginTime());
        result.setResponsible(findExamPaperVo.getResponsible());
        result.setInvalidEndTime(findExamPaperVo.getInvalidEndTime());
        return result;
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
