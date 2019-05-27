package com.bishe.portal.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.dao.*;
import com.bishe.portal.model.mo.*;
import com.bishe.portal.model.vo.*;
import com.bishe.portal.service.ExamPaperMiddleInfoService;
import com.bishe.portal.service.enums.ExamMiddleTypeEnum;
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
    public ExamPaperMiddleInfoVo getExamPaperInfo(String examPaperNum,int page,int pageSize) {
        ExamPaperMiddleInfoVo examPaperVo = new ExamPaperMiddleInfoVo();
        examPaperVo.setExamPaperNumber(examPaperNum);
        TbExamPaper examPaper = tbExamPaperDao.getExamPaperByNumber(examPaperNum);
        if(examPaper!=null) {
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
            examPaperVo.setExamTime(examPaper.getExamTime());
            examPaperVo.setExamPaperScore(examPaper.getExamJudgeScore() + examPaper.getExamSelectScore());
            List<ExamSelectInfoVo> selectInfoVos = new ArrayList<>();
            List<ExamJudgeInfoVo> examJudgeInfoVos = new ArrayList<>();
            int startIndex = (page-1)*pageSize;
            List<String> subjectIdList = tbExamPaperMiddleDao.getSubjectIdByExamPaperNum(examPaperNum, startIndex, pageSize);
            if (subjectIdList!=null&&subjectIdList.size()>0) {
                List<TbExamJudge> examJudgeList = tbExamJudgeDao.getExamJudgeByNumbers(subjectIdList);
                if (examJudgeList != null && examJudgeList.size() > 0) {
                    for (TbExamJudge tbExamJudge : examJudgeList) {
                        ExamJudgeInfoVo examJudgeInfoVo = getExamJudgeInfoVo(tbExamJudge);
                        examJudgeInfoVos.add(examJudgeInfoVo);
                    }
                }
                List<TbExamSelect> examSelectList = tbExamSelectDao.getExamSelectByNumbers(subjectIdList);
                if(examSelectList!=null&&examSelectList.size()>0) {
                    for (TbExamSelect tbExamSelect : examSelectList) {
                        ExamSelectInfoVo examSelectInfoVo = getExamSelectInfoVo(tbExamSelect);
                        List<TbExamSelectOption> selectOptionList = tbExamSelectOptionDao.getSelectOptionList(tbExamSelect.getSubjectId());
                        List<ExamSelectOptionInfoVo> selectOptionInfoVos = new ArrayList<>();
                        if(selectOptionList!=null&&selectOptionList.size()>0) {
                            for (TbExamSelectOption tbExamSelectOption : selectOptionList) {
                                if(tbExamSelectOption.getExamSelect().equals("8120")) {
                                System.out.println();
                                }
                                ExamSelectOptionInfoVo examSelectOptionInfoVo = getExamSelectOptionVo(tbExamSelectOption);
                                selectOptionInfoVos.add(examSelectOptionInfoVo);
                            }
                        }
                        examSelectInfoVo.setSelectOptionInfos(selectOptionInfoVos);
                        selectInfoVos.add(examSelectInfoVo);
                    }
                }
            }
            examPaperVo.setExamSelectInfoVos(selectInfoVos);
            examPaperVo.setExamJudgeInfoVos(examJudgeInfoVos);
        }
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
    public ExamSelectInfoVo addParamSelect(ExamSelectInfoVo selectInfoVo) {
        TbExamSelect tbExamSelect = getTbExamSelect(selectInfoVo);
        tbExamSelect.setSubjectId(UUIDUtils.getUUID(4));
        tbExamSelectDao.insertExamSelectInfo(tbExamSelect);
        TbExamSelect examSelectBySubject = tbExamSelectDao.getExamSelectBySubject(tbExamSelect.getSubjectId());
        ExamSelectInfoVo examSelectInfoVo = getExamSelectInfoVo(examSelectBySubject);
        List<ExamSelectOptionInfoVo> selectOptionInfos = selectInfoVo.getSelectOptionInfos();
        for (ExamSelectOptionInfoVo examSelectOptionInfoVo:selectOptionInfos){
            TbExamSelectOption tbExamSelectOption = getTbExamSelectOption(examSelectOptionInfoVo);
            tbExamSelectOption.setExamSelect(tbExamSelect.getSubjectId());
            tbExamSelectOptionDao.insertExamSelectOption(tbExamSelectOption);
        }
        examSelectInfoVo.setSelectOptionInfos(selectOptionInfos);
        return examSelectInfoVo;
    }

    @Override
    public ExamJudgeInfoVo addParamJudge(ExamJudgeInfoVo judgeInfoVo) {
        TbExamJudge tbExamJudge = getTbExamJudge(judgeInfoVo);
        tbExamJudge.setSubjectId(UUIDUtils.getUUID(4));
        tbExamJudgeDao.insertExamJudgeInfo(tbExamJudge);
        TbExamJudge examJudgeBySubject = tbExamJudgeDao.getExamJudgeBySubject(tbExamJudge.getSubjectId());
        return getExamJudgeInfoVo(examJudgeBySubject);

    }

    @Override
    public PageShowVo getAllSelectExamPaperSubject(PageShowVo pageShowVo) {
        int startIndex  = (pageShowVo.getPage()-1)*pageShowVo.getPageSize();
        List<TbExamSelect> selects = tbExamSelectDao.getAllSelectSubject(startIndex,pageShowVo.getPageSize());
        List<ExamSelectInfoVo> selectInfoVos = new ArrayList<>();
        for (TbExamSelect tbExamSelect : selects){
            ExamSelectInfoVo examSelectInfoVo = getExamSelectInfoVo(tbExamSelect);
            List<ExamSelectOptionInfoVo> selectOptionInfoVos = new ArrayList<>();
            List<TbExamSelectOption> selectOptionList = tbExamSelectOptionDao.getSelectOptionList(tbExamSelect.getSubjectId());
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
        examSelectInfoVo.setExamMiddleType(tbExamSelect.getExamMiddleType());
        examSelectInfoVo.setExamMiddleTypeName(ExamMiddleTypeEnum.getTypeNameStatus(tbExamSelect.getExamMiddleType()));
        return examSelectInfoVo;
    }

    private ExamJudgeInfoVo getExamJudgeInfoVo(TbExamJudge tbExamJudge) {
        ExamJudgeInfoVo examJudgeInfoVo = new ExamJudgeInfoVo();
        examJudgeInfoVo.setExamAnswer(String.valueOf(tbExamJudge.getExamAnswer()));
        examJudgeInfoVo.setExamTittle(tbExamJudge.getExamTittle());
        examJudgeInfoVo.setScore(tbExamJudge.getScore());
        examJudgeInfoVo.setSubjectId(tbExamJudge.getSubjectId());
        examJudgeInfoVo.setExamSubjectType(2);
        examJudgeInfoVo.setExamSubjectType(tbExamJudge.getExamMiddleType());
        examJudgeInfoVo.setExamMiddleTypeName(ExamMiddleTypeEnum.getTypeNameStatus(tbExamJudge.getExamMiddleType()));
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
        tbExamSelect.setExamMiddleType(examSelectInfoVo.getExamMiddleType());
        return tbExamSelect;
    }

    private TbExamJudge getTbExamJudge(ExamJudgeInfoVo examJudgeInfoVo) {
        TbExamJudge tbExamJudge = new TbExamJudge();
        tbExamJudge.setExamAnswer(Integer.valueOf(examJudgeInfoVo.getExamAnswer()));
        tbExamJudge.setExamTittle(examJudgeInfoVo.getExamTittle());
        tbExamJudge.setScore(examJudgeInfoVo.getScore());
        tbExamJudge.setExamMiddleType(examJudgeInfoVo.getExamMiddleType());
        return tbExamJudge;
    }
}
