package com.bishe.portal.service.impl;

import com.bishe.portal.dao.*;
import com.bishe.portal.model.mo.*;
import com.bishe.portal.model.vo.*;
import com.bishe.portal.service.ExamStartService;
import com.bishe.portal.service.utils.QiniuWrapper;
import com.bishe.portal.service.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamStartServiceImpl implements ExamStartService {
    @Resource
    TbExamStartDao examStartDao;
    @Resource
    TbExamChooseDao examChooseDao;
    @Resource
    TbExamSelectDao tbExamSelectDao;
    @Resource
    TbExamJudgeDao tbExamJudgeDao;
    @Resource
    TbExamPaperDao examPaperDao;
    @Resource
    TbUsersDao tbUsersDao;
    @Resource
    TbColumnManageDao tbColumnManageDao;
    @Resource
    TbExamPaperMiddleDao tbExamPaperMiddleDao;
    @Resource
    TbExamSelectOptionDao tbExamSelectOptionDao;
    private static final Logger logger = LoggerFactory.getLogger(ExamStartServiceImpl.class);


    @Override
    public ExamPaperStartMiddleInfoVo startExam(String examPaperNum, String account) {
        ExamPaperStartMiddleInfoVo end = new ExamPaperStartMiddleInfoVo();
        TbExamStart examStart = new TbExamStart();
        examStart.setExaminee(account);
        examStart.setExamPaperNum(examPaperNum);
        examStart.setExamStartNum(UUIDUtils.getUUID(8));
        examStart.setScore(0);
        examStartDao.insertExamStart(examStart);
        TbExamStart tbExamStart = examStartDao.getExamStartByNum(examStart.getExamStartNum());
        if (tbExamStart != null) {
            ExamStartVo result = getExamStartVo(tbExamStart);
            end.setAllScore(result.getAllScore());
            end.setBeginTime(result.getBeginTime());
            end.setEndTime(result.getEndTime());
            end.setExamineeId(result.getExaminee());
            TbUsers userInfo = tbUsersDao.getUserInfoByAccount(end.getExamineeId());
            end.setExamineeName(userInfo == null ? "" : userInfo.getName());
            TbExamPaper examPaper = examPaperDao.getExamPaperByNumber(examPaperNum);
            if (examPaper != null) {
                end.setExamSelectScore(examPaper.getExamSelectScore());
                end.setExamJudgeScore(examPaper.getExamJudgeScore());
                end.setCreateUnit(examPaper.getCreateUnit());
                end.setExamPaperNumber(examPaper.getExamPaperNum());
                end.setExamStartNum(result.getExamStartNum());
                end.setExamPaperType(examPaper.getExamPaperType());
                end.setExamTime(examPaper.getExamTime());
                TbColumnManage columnInfo = tbColumnManageDao.getColumnInfoById(String.valueOf(examPaper.getExamPaperType()));
                if (columnInfo != null) {
                    end.setExamPaperTypeName(columnInfo.getColumnName());
                    end.setAllScore(examPaper.getExamJudgeScore() + examPaper.getExamSelectScore());
                    end.setExamPaperName(examPaper.getExamPaperName());
                    end.setExamPaperCount(examPaper.getExamPaperCount());
                }
                List<String> subjectList = tbExamPaperMiddleDao.getSubjectIdByExamPaperNum(end.getExamPaperNumber());
                if((subjectList!=null)&&(subjectList.size()>0)) {
                    List<TbExamSelect> selectList = tbExamSelectDao.getExamSelectByNumbers(subjectList);
                    List<TbExamJudge> judgeList = tbExamJudgeDao.getExamJudgeByNumbers(subjectList);
                    List<ExamJudgeInfoVo> judgeVoList = new ArrayList<>();
                    List<ExamSelectInfoVo> selectVoList = new ArrayList<>();
                    if (judgeList!=null&&judgeList.size()>0) {
                        for (TbExamJudge tbExamJudge : judgeList) {
                            ExamJudgeInfoVo examJudgeInfoVo = getExamJudgeInfoVo(tbExamJudge);
                            judgeVoList.add(examJudgeInfoVo);
                        }
                    }
                    if (selectList!=null&&subjectList.size()>0) {
                        for (TbExamSelect tbExamSelect : selectList) {
                            ExamSelectInfoVo examSelectInfoVo = getExamSelectInfoVo(tbExamSelect);
                            List<ExamSelectOptionInfoVo> selectOptionInfoVos = new ArrayList<>();
                            List<TbExamSelectOption> selectOptionList = tbExamSelectOptionDao.getSelectOptionList(tbExamSelect.getSubjectId());
                            if (selectOptionList!=null&&selectOptionList.size()>0) {
                                for (TbExamSelectOption tbExamSelectOption : selectOptionList) {
                                    ExamSelectOptionInfoVo examSelectOptionInfoVo = getExamSelectOptionVo(tbExamSelectOption);
                                    selectOptionInfoVos.add(examSelectOptionInfoVo);
                                }
                            }
                            examSelectInfoVo.setSelectOptionInfos(selectOptionInfoVos);
                            selectVoList.add(examSelectInfoVo);
                        }
                    }
                    end.setExamSelectInfoVos(selectVoList);
                    end.setExamJudgeInfoVos(judgeVoList);
                }
            }
        }
        return end;

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
    private ExamStartVo getExamStartVo(TbExamStart tbExamStart) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ExamStartVo examStartVo = new ExamStartVo();
        examStartVo.setBeginTime(sdf.format(tbExamStart.getCreateAt()));
        examStartVo.setEndTime(sdf.format(tbExamStart.getUpdateAt()));
        examStartVo.setExaminee(tbExamStart.getExaminee());
        examStartVo.setExamPaperNum(tbExamStart.getExamPaperNum());
        examStartVo.setExamStartNum(tbExamStart.getExamStartNum());
        TbExamPaper examPaper = examPaperDao.getExamPaperByNumber(tbExamStart.getExamPaperNum());
        examStartVo.setAllScore(examPaper.getExamJudgeScore()+examPaper.getExamSelectScore());
        examStartVo.setScore(tbExamStart.getScore());
        return examStartVo;
    }

    @Override
    public ExamStartVo endExam(String examStartNum) {
        TbExamStart tbExamStart = examStartDao.getExamStartByNum(examStartNum);
        List<TbExamChoose> chooseList = examChooseDao.getCorrectChoose(tbExamStart.getExamStartNum());
        int score = tbExamStart.getScore();
        if(chooseList != null &&chooseList.size()>0) {
            for (TbExamChoose tbExamChoose : chooseList) {
                if (tbExamChoose.getExamSubjectType() == 1) {
                    TbExamSelect examSelectBySubject = tbExamSelectDao.getExamSelectBySubject(tbExamChoose.getSubjectId());
                    score += examSelectBySubject.getScore();
                }
                if (tbExamChoose.getExamSubjectType() == 2) {
                    TbExamJudge examJudge = tbExamJudgeDao.getExamJudgeBySubject(tbExamChoose.getSubjectId());
                    score += examJudge.getScore();
                }
            }
        }
        tbExamStart.setScore(score);
        examStartDao.updateScore(score,examStartNum);
        ExamStartVo examStartVo = getExamStartVo(tbExamStart);
        return examStartVo;
    }

    @Override
    public void chooseExamSubject(ExamChooseVo examChooseVo) {
        TbExamChoose tbExamChoose = examChooseDao.getExamChooseByNum(examChooseVo.getSubjectId(),examChooseVo.getExamStartNum());
        if (tbExamChoose == null){
            examChooseDao.insertExamChoose(getExamChoose(examChooseVo));
        }else{
            if ((tbExamChoose.getAnswer() ==null)||!tbExamChoose.getAnswer().equals(examChooseVo.getAnswer())){
                if (examChooseVo.getAnswer()!=null) {
                    examChooseDao.updateAnswer(examChooseVo.getExamStartNum(), examChooseVo.getSubjectId(), examChooseVo.getAnswer());
                }
            }
        }
    }

    @Override
    public void ChooseExamSubjectList(List<ExamChooseVo> examChooseList, String examStartNum) {
        List<TbExamChoose> chooseList = examChooseDao.getChooseByStartNum(examStartNum);
        Map<String,TbExamChoose> chooseMap = getChooseMap(chooseList);
        List<TbExamChoose> insertChooseList = new ArrayList<>();
        List<TbExamChoose> updateChooseList = new ArrayList<>();
        for (ExamChooseVo examChooseVo:examChooseList){
            examChooseVo.setExamStartNum(examStartNum);
            if (chooseMap.containsKey(examChooseVo.getSubjectId())){
                updateChooseList.add(getExamChoose(examChooseVo));
            }else{
                insertChooseList.add(getExamChoose(examChooseVo));
            }
        }
        for (TbExamChoose tbExamChoose:insertChooseList){
            examChooseDao.insertExamChoose(tbExamChoose);
        }
        for (TbExamChoose tbExamChoose:updateChooseList){
            examChooseDao.updateAnswer(tbExamChoose.getExamStartNum(),tbExamChoose.getSubjectId(),tbExamChoose.getAnswer());
        }
    }

    @Override
    public List<ExamStartVo> getAllExamHistory(int page, int pageSize) {
        List<TbExamStart> examStartList = examStartDao.getFinishExamInfo((page-1)*pageSize,pageSize);
        List<ExamStartVo> result = new ArrayList<>();
        for (TbExamStart tbExamStart : examStartList){
            result.add(getExamStartVo(tbExamStart));
        }
        return result;
    }

    private Map<String,TbExamChoose> getChooseMap(List<TbExamChoose> chooseList) {
        Map<String,TbExamChoose> chooseMap = new HashMap<>();
        for (TbExamChoose tbExamChoose : chooseList){
            chooseMap.put(tbExamChoose.getSubjectId(),tbExamChoose);
        }
        return chooseMap;
    }

    private TbExamChoose getExamChoose(ExamChooseVo examChooseVo) {
        TbExamChoose tbExamChoose = new TbExamChoose();
        tbExamChoose.setAnswer(examChooseVo.getAnswer());
        tbExamChoose.setExamStartNum(examChooseVo.getExamStartNum());
        tbExamChoose.setExamSubjectType(examChooseVo.getExamSubjectType());
        if (examChooseVo.getExamSubjectType()==1){
            TbExamSelect tbExamSelect = tbExamSelectDao.getExamSelectBySubject(examChooseVo.getSubjectId());
            if (tbExamSelect.getExamAnswer().equals(examChooseVo.getAnswer())){
                tbExamChoose.setIsCorrect(1);
            }else {
                tbExamChoose.setIsCorrect(0);
            }
        }
        if (examChooseVo.getExamSubjectType()==2){
            TbExamJudge tbExamJudge = tbExamJudgeDao.getExamJudgeBySubject(examChooseVo.getSubjectId());
            if (tbExamJudge.getExamAnswer()== Integer.valueOf(examChooseVo.getAnswer().trim())){
                tbExamChoose.setIsCorrect(1);
            }else {
                tbExamChoose.setIsCorrect(0);
            }
        }
        tbExamChoose.setSubjectId(examChooseVo.getSubjectId());
        return tbExamChoose;
    }
}
