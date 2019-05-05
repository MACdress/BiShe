package com.bishe.portal.service.impl;

import com.bishe.portal.dao.*;
import com.bishe.portal.model.mo.*;
import com.bishe.portal.model.vo.ExamChooseVo;
import com.bishe.portal.model.vo.ExamStartVo;
import com.bishe.portal.service.ExamStartService;
import com.bishe.portal.service.utils.UUIDUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

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

    @Override
    public ExamStartVo startExam(String examPaperNum, String account) {
        TbExamStart examStart = new TbExamStart();
        examStart.setExaminee(account);
        examStart.setExamPaperNum(examPaperNum);
        examStart.setExamStartNum(UUIDUtils.getUUID(8));
        examStart.setScore(0);
        examStartDao.insertExamStart(examStart);
        TbExamStart tbExamStart = examStartDao.getExamStartByNum(examStart.getExamStartNum());
        ExamStartVo result = getExamStartVo(tbExamStart);
        TbExamPaper examPaper = examPaperDao.getExamPaperByNumber(examPaperNum);
        result.setAllScore(examPaper.getExamJudgeScore()+examPaper.getExamSelectScore());
        return result;
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
        return examStartVo;
    }

    @Override
    public ExamStartVo endExam(String examStartNum) {
        TbExamStart tbExamStart = examStartDao.getExamStartByNum(examStartNum);
        List<TbExamChoose> chooseList = examChooseDao.getCorrectChoose(tbExamStart.getExamStartNum());
        int score = tbExamStart.getScore();
        for (TbExamChoose tbExamChoose : chooseList){
            if(tbExamChoose.getExamSubjectType() == 1){
                TbExamSelect examSelectBySubject = tbExamSelectDao.getExamSelectBySubject(tbExamChoose.getSubjectId());
                score += examSelectBySubject.getScore();
            }
            if(tbExamChoose.getExamSubjectType() == 2){
                TbExamJudge examJudge = tbExamJudgeDao.getExamJudgeBySubject(tbExamChoose.getSubjectId());
                score += examJudge.getScore();
            }
        }
        tbExamStart.setScore(score);
        examStartDao.updateScore(score,examStartNum);
        TbExamStart examStart= examStartDao.getExamStartByNum(examStartNum);
        ExamStartVo examStartVo = getExamStartVo(examStart);
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
