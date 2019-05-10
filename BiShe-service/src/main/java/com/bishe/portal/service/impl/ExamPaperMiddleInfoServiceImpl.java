package com.bishe.portal.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.dao.*;
import com.bishe.portal.model.mo.*;
import com.bishe.portal.model.vo.*;
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
    public void addQuestionInExamPaper(AddExamPaperMiddleVo addExamPaperMiddleVo) {
        String examPaperNum = addExamPaperMiddleVo.getExamPaperNum();
        if (StringUtils.isEmpty(examPaperNum)){
            return;
        }
        TbExamPaper examPaper = tbExamPaperDao.getExamPaperByNumber(examPaperNum);
        int judgeGrade = examPaper.getExamJudgeScore();
        int selectGrade = examPaper.getExamSelectScore();
        int count = examPaper.getExamPaperCount();
        List <TbExamPaperMiddle> examQuestionList = new ArrayList<>();
        List<SimpleSubjectVo> subjectList = addExamPaperMiddleVo.getSubjectList();
        if (subjectList.size()==0){
            return;
        }
        for (SimpleSubjectVo simpleSubjectVo : subjectList){
            if(StringUtils.isEmpty(simpleSubjectVo.getSubjectId())){
                continue;
            }
            if((simpleSubjectVo.getExamSubjectType()!=null)&&(simpleSubjectVo.getExamSubjectType()==1)){
                TbExamSelect examSelectBySubject = tbExamSelectDao.getExamSelectBySubject(simpleSubjectVo.getSubjectId());
                selectGrade+= examSelectBySubject.getScore();
                TbExamPaperMiddle tbExamPaperMiddle  = new TbExamPaperMiddle();
                tbExamPaperMiddle.setExamSubjectId(simpleSubjectVo.getSubjectId());
                tbExamPaperMiddle.setExamPaperNum(examPaperNum);
                examQuestionList.add(tbExamPaperMiddle);
            }
            if((simpleSubjectVo.getExamSubjectType()!=null)&&(simpleSubjectVo.getExamSubjectType()==2)){
                TbExamJudge examJudgeBySubject = tbExamJudgeDao.getExamJudgeBySubject(simpleSubjectVo.getSubjectId());
                judgeGrade+=examJudgeBySubject.getScore();
                TbExamPaperMiddle tbExamPaperMiddle  = new TbExamPaperMiddle();
                tbExamPaperMiddle.setExamSubjectId(simpleSubjectVo.getSubjectId());
                tbExamPaperMiddle.setExamPaperNum(examPaperNum);
                examQuestionList.add(tbExamPaperMiddle);
            }
            count++;
        }
        examPaper.setExamPaperCount(count);
        examPaper.setExamJudgeScore(judgeGrade);
        examPaper.setExamSelectScore(selectGrade);
        tbExamPaperDao.updateExamPaper(examPaper);
        System.out.println(examQuestionList.size());
        if (examQuestionList.size()>0) {
            for (TbExamPaperMiddle tbExamPaperMiddle : examQuestionList) {
                tbExamPaperMiddleDao.insertTbExamPaperMiddle(tbExamPaperMiddle);
            }
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
        examPaperVo.setExamPaperNumber(examPaper.getExamPaperNum());
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
    public void removeExamPaperSubject(String examPaperNum, List<SimpleSubjectVo> subjectId) {
        List<String> subjectList = new ArrayList<>();
        for (SimpleSubjectVo simpleSubjectVo:subjectId){
            subjectList.add(simpleSubjectVo.getSubjectId());
        }
        tbExamPaperMiddleDao.deleteExamPaperSubject(examPaperNum,subjectList);
    }

    @Override
    public void addParamSelect(ExamSelectInfoVo selectInfoVo) {
        TbExamSelect tbExamSelect = getTbExamSelect(selectInfoVo);
        tbExamSelect.setSubjectId(UUIDUtils.getUUID(4));
        tbExamSelectDao.insertExamSelectInfo(tbExamSelect);
        TbExamSelect examSelectBySubject = tbExamSelectDao.getExamSelectBySubject(tbExamSelect.getSubjectId());
        ExamSelectInfoVo examSelectInfoVo = getExamSelectInfoVo(examSelectBySubject);
        List<ExamSelectOptionInfoVo> selectOptionInfos = selectInfoVo.getSelectOptionInfos();
        for (ExamSelectOptionInfoVo examSelectOptionInfoVo:selectOptionInfos){
            tbExamSelectOptionDao.insertExamSelectOption(getTbExamSelectOption(examSelectOptionInfoVo));

        }
    }

    @Override
    public void addParamJudge(ExamJudgeInfoVo judgeInfoVo) {
        tbExamJudgeDao.insertExamJudgeInfo(getTbExamJudge(judgeInfoVo));
    }

    @Override
    public PageShowVo getAllSelectExamPaperSubject(PageShowVo pageShowVo) {
        int startIndex  = (pageShowVo.getPage()-1)*pageShowVo.getPageSize();
        List<TbExamSelect> selects = tbExamSelectDao.getAllSelectSubject(startIndex,pageShowVo.getPageSize());
        List<ExamSelectInfoVo> selectInfoVos = new ArrayList<>();
        for (TbExamSelect tbExamSelect : selects){
            ExamSelectInfoVo examSelectInfoVo = getExamSelectInfoVo(tbExamSelect);
            List<ExamSelectOptionInfoVo> selectOptionInfoVos = new ArrayList<>();
            List<TbExamSelectOption> selectOptionList = tbExamSelectOptionDao.getSelectOptionList(tbExamSelect.getId());
            for (TbExamSelectOption tbExamSelectOption:selectOptionList){
                ExamSelectOptionInfoVo examSelectOptionInfoVo = getExamSelectOptionVo(tbExamSelectOption);
                selectOptionInfoVos.add(examSelectOptionInfoVo);
            }
            examSelectInfoVo.setSelectOptionInfos(selectOptionInfoVos);
            selectInfoVos.add(examSelectInfoVo);
        }
        PageShowVo pageShowVo1 = new PageShowVo();
        pageShowVo1.setPageSize(pageShowVo.getPageSize());
        pageShowVo1.setPage(pageShowVo.getPage());
        pageShowVo1.setData(selectInfoVos);
        return pageShowVo1;
    }

    @Override
    public PageShowVo getAllJudgeExamPaperSubject(PageShowVo pageShowVo) {
        int startIndex  = (pageShowVo.getPage()-1)*pageShowVo.getPageSize();
        List<TbExamJudge> judges = tbExamJudgeDao.getAllJudgeSubject(startIndex,pageShowVo.getPageSize());
        List<ExamJudgeInfoVo> examJudgeInfoVos  = new ArrayList<>();
        for (TbExamJudge tbExamJudge :judges){
            ExamJudgeInfoVo examJudgeInfoVo  = getExamJudgeInfoVo(tbExamJudge);
            examJudgeInfoVos.add(examJudgeInfoVo);
        }
        PageShowVo pageShowVo1 = new PageShowVo();
        pageShowVo1.setPageSize(pageShowVo.getPageSize());
        pageShowVo1.setPage(pageShowVo.getPage());
        pageShowVo1.setData(examJudgeInfoVos);
        return pageShowVo1;
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
        examSelectInfoVo.setExamSubjectType(1);
        return examSelectInfoVo;
    }

    private ExamJudgeInfoVo getExamJudgeInfoVo(TbExamJudge tbExamJudge) {
        ExamJudgeInfoVo examJudgeInfoVo = new ExamJudgeInfoVo();
        examJudgeInfoVo.setExamAnswer(String.valueOf(tbExamJudge.getExamAnswer()));
        examJudgeInfoVo.setExamTittle(tbExamJudge.getExamTittle());
        examJudgeInfoVo.setScore(tbExamJudge.getScore());
        examJudgeInfoVo.setSubjectId(tbExamJudge.getSubjectId());
        examJudgeInfoVo.setExamSubjectType(2);
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
