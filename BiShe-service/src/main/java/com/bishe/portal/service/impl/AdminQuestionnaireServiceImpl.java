package com.bishe.portal.service.impl;

import com.bishe.portal.dao.*;
import com.bishe.portal.model.mo.*;
import com.bishe.portal.model.vo.ExamSelectOptionInfoVo;
import com.bishe.portal.model.vo.QuestionnairePaperVo;
import com.bishe.portal.model.vo.QuestionnaireSelectVo;
import com.bishe.portal.service.AdminQuestionnaireService;
import com.bishe.portal.service.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminQuestionnaireServiceImpl implements AdminQuestionnaireService {
    @Resource
    TbQuestionnaireDao tbQuestionnaireDao;
    @Resource
    TbQuestionnaireSelectDao tbQuestionnaireSelectDao;
    @Resource
    TbExamSelectOptionDao tbExamSelectOptionDao;
    @Resource
    TbQuestionnaireAnswerDao tbQuestionnaireAnswerDao;
    @Resource
    TbUsersDao tbUsersDao;
    @Resource
    TbUserEventDao tbUserEventDao;
    private static final Logger logger = LoggerFactory.getLogger(AdminQuestionnaireServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuestionnairePaperVo addQuestionnaire(String questionnaireName) {
        TbQuestionnaire tbQuestionnaire = new TbQuestionnaire();
        tbQuestionnaire.setQuestionnaireName(questionnaireName);
        tbQuestionnaire.setQuestionnaireNum(UUIDUtils.getUUID(8));
        tbQuestionnaireDao.insertQuestionnaire(tbQuestionnaire);
        TbQuestionnaire tbQuestionnaire1 = tbQuestionnaireDao.selectQuestionnaireByNum(tbQuestionnaire.getQuestionnaireNum());
        System.out.println(tbQuestionnaire1);
        return getQuestionnairePaperVo(tbQuestionnaire1);
    }

    private QuestionnairePaperVo getQuestionnairePaperVo(TbQuestionnaire tbQuestionnaire) {
        QuestionnairePaperVo questionnairePaperVo = new QuestionnairePaperVo();
        questionnairePaperVo.setQuestionnaireName(tbQuestionnaire.getQuestionnaireName());
        questionnairePaperVo.setQuestionnaireNum(tbQuestionnaire.getQuestionnaireNum());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        questionnairePaperVo.setCreateTime(simpleDateFormat.format(tbQuestionnaire.getCreateAt()));
        return questionnairePaperVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuestionnaireSelectVo addQuestion(QuestionnaireSelectVo questionnaireSelectVo) {
        TbQuestionnaireSelect tbQuestionnaireSelect = getTbQuestionnaireSelect(questionnaireSelectVo);
        tbQuestionnaireSelect.setQuestionId(UUIDUtils.getUUID(4));
        tbQuestionnaireSelectDao.insertQuestionnaireSelect(tbQuestionnaireSelect);
        tbQuestionnaireSelect = tbQuestionnaireSelectDao.getQuestionnaireSelectById(tbQuestionnaireSelect.getQuestionId());
        List<ExamSelectOptionInfoVo> options = questionnaireSelectVo.getOptions();
        if((options!=null)&&(options.size()>0)){
            for (ExamSelectOptionInfoVo examSelectOptionInfoVo : options) {
                TbExamSelectOption tbExamSelectOption = new TbExamSelectOption();
                tbExamSelectOption.setExamSelect(tbQuestionnaireSelect.getQuestionId());
                tbExamSelectOption.setOption(examSelectOptionInfoVo.getOption());
                tbExamSelectOption.setOptionContent(examSelectOptionInfoVo.getOptionContent());
                tbExamSelectOptionDao.insertExamSelectOption(tbExamSelectOption);
            }
        }
        QuestionnaireSelectVo result = getQuestionnaireSelectVo(tbQuestionnaireSelect);
        result.setOptions(options);
        return result;
    }

    private QuestionnaireSelectVo getQuestionnaireSelectVo(TbQuestionnaireSelect tbQuestionnaireSelect) {
        QuestionnaireSelectVo questionnaireSelectVo = new QuestionnaireSelectVo();
        questionnaireSelectVo.setQuestion(tbQuestionnaireSelect.getQuestion());
        questionnaireSelectVo.setQuestionnaireNum(tbQuestionnaireSelect.getQuestionnaireNum());
        questionnaireSelectVo.setQuestionId(tbQuestionnaireSelect.getQuestionId());
        return questionnaireSelectVo;
    }

    private TbQuestionnaireSelect getTbQuestionnaireSelect(QuestionnaireSelectVo questionnaireSelectVo) {
        TbQuestionnaireSelect tbQuestionnaireSelect = new TbQuestionnaireSelect();
        tbQuestionnaireSelect.setQuestion(questionnaireSelectVo.getQuestion());
        tbQuestionnaireSelect.setQuestionnaireNum(questionnaireSelectVo.getQuestionnaireNum());
        return tbQuestionnaireSelect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeQuestion(List<QuestionnaireSelectVo> questionnaireList) {
        List <String> questionIdList = new ArrayList<>();
        for (QuestionnaireSelectVo questionnaireSelectVo:questionnaireList){
            questionIdList.add(questionnaireSelectVo.getQuestionId());
        }
        tbQuestionnaireSelectDao.deleteQuestion(questionIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestionnaire(String questionnaireNum) {
        tbQuestionnaireDao.deleteQuestionnaire(questionnaireNum);
        tbQuestionnaireSelectDao.deleteQuestionByNum(questionnaireNum);
    }

    @Override
    public QuestionnairePaperVo getQuestionnaire(String questionnaireNum, Integer page, Integer pageSize, String account) {
        TbQuestionnaire tbQuestionnaire = tbQuestionnaireDao.selectQuestionnaireByNum(questionnaireNum);
        int startIndex = (page-1)*pageSize;
        QuestionnairePaperVo result = getQuestionnairePaperVo(tbQuestionnaire);
        List<QuestionnaireSelectVo> questionnaireSelectList = new ArrayList<>();
        List<TbQuestionnaireSelect> selectList = tbQuestionnaireSelectDao.getQuestionnaireSelectByNum(questionnaireNum,startIndex,pageSize);
        if ((selectList!=null)&&(selectList.size()>0)){
            for (TbQuestionnaireSelect tbQuestionnaireSelect : selectList) {
                QuestionnaireSelectVo selectVo = getQuestionnaireSelectVo(tbQuestionnaireSelect);
                List<TbExamSelectOption> selectOptionList = tbExamSelectOptionDao.getSelectOptionList(selectVo.getQuestionId());
                List<ExamSelectOptionInfoVo> optionInfoVos = new ArrayList<>();
                if ((selectOptionList!=null)&&(selectOptionList.size()>0)){
                    for (TbExamSelectOption tbExamSelectOption : selectOptionList){
                        optionInfoVos.add(getExamSelectOptionVo(tbExamSelectOption));
                    }
                }
                selectVo.setOptions(optionInfoVos);
                if(!StringUtils.isEmpty(account)) {
                    TbQuestionnaireAnswer answer = tbQuestionnaireAnswerDao.selectAnswerById(selectVo.getQuestionId(), account);
                    if (answer != null) {
                        selectVo.setAnswer(answer.getQuestionnaireAnswer());
                    }
                }
                questionnaireSelectList.add(selectVo);
            }
        }else {
            logger.info("获取选择题列表失败");
        }
        result.setSelectList(questionnaireSelectList);
        return result;
    }

    private ExamSelectOptionInfoVo getExamSelectOptionVo(TbExamSelectOption tbExamSelectOption) {
        ExamSelectOptionInfoVo examSelectOptionInfoVo = new ExamSelectOptionInfoVo();
        examSelectOptionInfoVo.setOption(tbExamSelectOption.getOption());
        examSelectOptionInfoVo.setOptionContent(tbExamSelectOption.getOptionContent());
        return examSelectOptionInfoVo;
    }

    @Override
    public List<QuestionnairePaperVo> getAllQuestionnaire() {
        List<TbQuestionnaire> questionnaireList = tbQuestionnaireDao.getAllQuestionnaire();
        List<QuestionnairePaperVo> result = new ArrayList<>();
        if((questionnaireList!=null)&&(questionnaireList.size()>0)){
            for (TbQuestionnaire tbQuestionnaire : questionnaireList){
                QuestionnairePaperVo paperVo = getQuestionnairePaperVo(tbQuestionnaire);
                result.add(paperVo);
            }
        }
        return result;
    }

    @Override
    public void chooseQuestion(List<QuestionnaireSelectVo> answers, String questionnaireNum,String account) {
        for (QuestionnaireSelectVo questionnaireSelectVo:answers){
            TbQuestionnaireAnswer tbQuestionnaireAnswer = new TbQuestionnaireAnswer();
            tbQuestionnaireAnswer.setQuestionnaireNum(questionnaireNum);
            tbQuestionnaireAnswer.setQuestionnaireAnswer(questionnaireSelectVo.getAnswer());
            tbQuestionnaireAnswer.setQuestionnaireId(questionnaireSelectVo.getQuestionId());
            tbQuestionnaireAnswer.setAccount(account);
            tbQuestionnaireAnswerDao.insertAnswer(tbQuestionnaireAnswer);
        }
    }

    @Override
    public List<QuestionnairePaperVo> getUserQuestion(String account) {
        List<TbQuestionnaire> questionnaireList = tbQuestionnaireDao.getAllQuestionnaire();
        List<QuestionnairePaperVo> result = new ArrayList<>();
        if((questionnaireList!=null)&&(questionnaireList.size()>0)){
            for (TbQuestionnaire tbQuestionnaire : questionnaireList){
                TbQuestionnaireAnswer answer = tbQuestionnaireAnswerDao.selectAnswerByNum(tbQuestionnaire.getQuestionnaireNum(),account);
                if (answer != null) {
                    QuestionnairePaperVo paperVo = getQuestionnairePaperVo(tbQuestionnaire);
                    result.add(paperVo);
                }else {
                    logger.info("答案为空");
                }
            }
        }
        return result;
    }

    @Override
    public void startAnswerQuestion(String questionnaireNum, String account) {
        TbUserEvent event = new TbUserEvent();
        event.setEventDate(new Date());
        event.setAccount(account);
        TbUsers userInfo = tbUsersDao.getUserInfoByAccount(account);
        TbQuestionnaire tbQuestionnaire = tbQuestionnaireDao.selectQuestionnaireByNum(questionnaireNum);
        event.setEvent(userInfo.getName()+"用户参加"+tbQuestionnaire.getQuestionnaireName()+"问卷调查");
        tbUserEventDao.insertEvent(event);
    }
}
