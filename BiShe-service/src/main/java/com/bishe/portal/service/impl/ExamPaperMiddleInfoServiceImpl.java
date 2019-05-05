package com.bishe.portal.service.impl;

import com.bishe.portal.dao.*;
import com.bishe.portal.model.mo.*;
import com.bishe.portal.model.vo.ExamJudgeInfoVo;
import com.bishe.portal.model.vo.ExamPaperMiddleInfoVo;
import com.bishe.portal.model.vo.ExamSelectInfoVo;
import com.bishe.portal.model.vo.ExamSelectOptionInfoVo;
import com.bishe.portal.service.ExamPaperMiddleInfoService;
import com.bishe.portal.service.utils.UUIDUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamPaperMiddleInfoServiceImpl implements ExamPaperMiddleInfoService {
    @Resource
    TbExamPaperDao tbExamPaperDao;
    @Resource
    TbExamJudgeDao tbExamJudgeDao;
    @Resource
    TbExamSelectDao tbExamSelectDao;
    @Resource
    TbExamSelectOptionDao tbExamSelectOptionDao;
    @Resource
    TbExamPaperMiddleDao tbExamPaperMiddleDao;

    @Override
    public void addQuestionInExamPaper(List<ExamSelectInfoVo> examSelectList, List<ExamJudgeInfoVo> judgeInfoList) {
        String examPaperNum = examSelectList.get(0).getExamPaperNum();
        TbExamPaper examPaper = tbExamPaperDao.getExamPaperByNumber(examPaperNum);
        int judgeGrade = examPaper.getExamJudgeScore();
        int selectGrade = examPaper.getExamSelectScore();
        int count = examPaper.getExamPaperCount();
        List <TbExamPaperMiddle> examQuestionList = new ArrayList<>();
        for (ExamJudgeInfoVo examJudgeInfoVo:judgeInfoList){
            TbExamJudge tbExamJudge = getTbExamJudge(examJudgeInfoVo);
            tbExamJudgeDao.insertExamJudgeInfo(tbExamJudge);
            TbExamPaperMiddle tbExamPaperMiddle = new TbExamPaperMiddle();
            tbExamPaperMiddle.setExamPaperNum(examPaperNum);
            tbExamPaperMiddle.setExamSubjectId(tbExamJudge.getSubjectId());
            examQuestionList.add(tbExamPaperMiddle);
            judgeGrade += examJudgeInfoVo.getScore();
            count++;
        }
        for (ExamSelectInfoVo examSelectInfoVo : examSelectList){
            List<ExamSelectOptionInfoVo> selectOptionInfos = examSelectInfoVo.getSelectOptionInfos();
            TbExamSelect tbExamSelect = getTbExamSelect(examSelectInfoVo);
            TbExamPaperMiddle tbExamPaperMiddle = new TbExamPaperMiddle();
            tbExamPaperMiddle.setExamPaperNum(examPaperNum);
            tbExamPaperMiddle.setExamSubjectId(tbExamSelect.getSubjectId());
            examQuestionList.add(tbExamPaperMiddle);
            tbExamSelectDao.insertExamSelectInfo(tbExamSelect);
            for (ExamSelectOptionInfoVo examSelectOptionInfo:selectOptionInfos){
                TbExamSelectOption tbExamSelectOption = getTbExamSelectOption(examSelectOptionInfo);
                tbExamSelectOption.setExamSelect(tbExamSelect.getId());
                tbExamSelectOptionDao.insertExamSelectOption(tbExamSelectOption);
            }
            selectGrade += examSelectInfoVo.getScore();
            count ++;
        }
        examPaper.setExamPaperCount(count);
        examPaper.setExamJudgeScore(judgeGrade);
        examPaper.setExamSelectScore(selectGrade);
        tbExamPaperDao.updateExamPaper(examPaper);
        for (TbExamPaperMiddle tbExamPaperMiddle : examQuestionList){
            tbExamPaperMiddleDao.insertTbExamPaperMiddle(tbExamPaperMiddle);
        }
    }

    @Override
    public ExamPaperMiddleInfoVo getExamPaperInfo(String examPaperNum) {
        ExamPaperMiddleInfoVo examPaperVo = new ExamPaperMiddleInfoVo();
        examPaperVo.setExamPaperNumber(examPaperNum);
        TbExamPaper examPaper = tbExamPaperDao.getExamPaperByNumber(examPaperNum);
        examPaperVo.setCreateUnit(examPaper.getCreateUnit());
        examPaperVo.setExamJudgeScore(examPaper.getExamJudgeScore());
        examPaperVo.setExamSelectScore(examPaper.getExamSelectScore());
        examPaperVo.setInvalidTime(examPaper.getInvalidTime());
        examPaperVo.setStatus(examPaper.getStatus());
        examPaperVo.setResponsible(examPaper.getResponsible());
        examPaperVo.setExamPaperType(examPaper.getExamPaperType());
        examPaperVo.setExamPaperName(examPaper.getExamPaperName());
        examPaperVo.setExamPaperCount(examPaper.getExamPaperCount());
        examPaperVo.setExamPaperScore(examPaper.getExamJudgeScore()+examPaper.getExamSelectScore());
        List<ExamSelectInfoVo> selectInfoVos = new ArrayList<>();
        List<ExamJudgeInfoVo> examJudgeInfoVos = new ArrayList<>();
        List<String> subjectIdList = tbExamPaperMiddleDao.getSubjectIdByExamPaperNum(examPaperNum);
        List<TbExamJudge> examJudgeList = tbExamJudgeDao.getExamJudgeByNumbers(subjectIdList);
        for (TbExamJudge tbExamJudge :examJudgeList){
            ExamJudgeInfoVo examJudgeInfoVo  = getExamJudgeInfoVo(tbExamJudge);
            examJudgeInfoVos.add(examJudgeInfoVo);
        }
        examPaperVo.setExamJudgeInfoVos(examJudgeInfoVos);
        List<TbExamSelect> examSelectList = tbExamSelectDao.getExamSelectByNumbers(subjectIdList);
        for (TbExamSelect tbExamSelect : examSelectList){
            ExamSelectInfoVo examSelectInfoVo = getExamSelectInfoVo(tbExamSelect);
            List<TbExamSelectOption> selectOptionList = tbExamSelectOptionDao.getSelectOptionList(tbExamSelect.getId());
            List<ExamSelectOptionInfoVo> selectOptionInfoVos = new ArrayList<>();
            for (TbExamSelectOption tbExamSelectOption:selectOptionList){
                ExamSelectOptionInfoVo examSelectOptionInfoVo = getExamSelectOptionVo(tbExamSelectOption);
                selectOptionInfoVos.add(examSelectOptionInfoVo);
            }
            examSelectInfoVo.setSelectOptionInfos(selectOptionInfoVos);
            selectInfoVos.add(examSelectInfoVo);
        }
        examPaperVo.setExamSelectInfoVos(selectInfoVos);
        return examPaperVo;
    }

    @Override
    public void removeExamPaperSubject(String examPaperNum, String subjectId) {
        tbExamPaperMiddleDao.deleteExamPaperSubject(examPaperNum,subjectId);
    }

    private ExamSelectOptionInfoVo getExamSelectOptionVo(TbExamSelectOption tbExamSelectOption) {
        ExamSelectOptionInfoVo examSelectOptionInfoVo = new ExamSelectOptionInfoVo();
        examSelectOptionInfoVo.setOption(tbExamSelectOption.getOption());
        examSelectOptionInfoVo.setOptionContent(tbExamSelectOption.getOptionContent());
        return examSelectOptionInfoVo;
    }

    private ExamSelectInfoVo getExamSelectInfoVo(TbExamSelect tbExamSelect) {
        ExamSelectInfoVo examSelectInfoVo = new ExamSelectInfoVo();
        examSelectInfoVo.setExamAnswer(tbExamSelect.getExamAnswer());
        examSelectInfoVo.setExamParse(tbExamSelect.getExamParse());
        examSelectInfoVo.setExamTittle(tbExamSelect.getExamTittle());
        examSelectInfoVo.setScore(tbExamSelect.getScore());
        examSelectInfoVo.setSubjectId(tbExamSelect.getSubjectId());
        examSelectInfoVo.setExamInfoType(1);
        return examSelectInfoVo;
    }

    private ExamJudgeInfoVo getExamJudgeInfoVo(TbExamJudge tbExamJudge) {
        ExamJudgeInfoVo examJudgeInfoVo = new ExamJudgeInfoVo();
        examJudgeInfoVo.setExamAnswer(String.valueOf(tbExamJudge.getExamAnswer()));
        examJudgeInfoVo.setExamTittle(tbExamJudge.getExamTittle());
        examJudgeInfoVo.setScore(tbExamJudge.getScore());
        examJudgeInfoVo.setSubjectId(tbExamJudge.getSubjectId());
        examJudgeInfoVo.setExamInfoType(2);
        return examJudgeInfoVo;
    }

    private TbExamSelectOption getTbExamSelectOption(ExamSelectOptionInfoVo examSelectOptionInfo) {
        TbExamSelectOption tbExamSelectOption = new TbExamSelectOption();
        tbExamSelectOption.setOption(examSelectOptionInfo.getOption());
        tbExamSelectOption.setOptionContent(examSelectOptionInfo.getOptionContent());
        return tbExamSelectOption;
    }

    private TbExamSelect getTbExamSelect(ExamSelectInfoVo examSelectInfoVo) {
        TbExamSelect tbExamSelect = new TbExamSelect();
        tbExamSelect.setExamAnswer(examSelectInfoVo.getExamAnswer());
        tbExamSelect.setExamParse(examSelectInfoVo.getExamParse());
        tbExamSelect.setScore(examSelectInfoVo.getScore());
        tbExamSelect.setExamTittle(examSelectInfoVo.getExamTittle());
        tbExamSelect.setSubjectId(UUIDUtils.getUUID(4));
        return tbExamSelect;
    }

    private TbExamJudge getTbExamJudge(ExamJudgeInfoVo examJudgeInfoVo) {
        TbExamJudge tbExamJudge = new TbExamJudge();
        tbExamJudge.setExamAnswer(Integer.valueOf(examJudgeInfoVo.getExamAnswer()));
        tbExamJudge.setExamTittle(examJudgeInfoVo.getExamTittle());
        tbExamJudge.setScore(examJudgeInfoVo.getScore());
        tbExamJudge.setSubjectId(UUIDUtils.getUUID(4));
        return tbExamJudge;
    }
}
