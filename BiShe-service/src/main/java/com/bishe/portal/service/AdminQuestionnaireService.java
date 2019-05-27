package com.bishe.portal.service;

import com.bishe.portal.model.vo.QuestionnairePaperVo;
import com.bishe.portal.model.vo.QuestionnaireSelectVo;

import java.util.List;

public interface AdminQuestionnaireService {
    QuestionnairePaperVo addQuestionnaire(String questionnaireName);

    QuestionnaireSelectVo addQuestion(QuestionnaireSelectVo questionnaireSelectVo);

    void removeQuestion(List<QuestionnaireSelectVo> questionnaireList);

    void deleteQuestionnaire(String questionnaireNum);

    QuestionnairePaperVo getQuestionnaire(String questionnaireNum, Integer page, Integer pageSize, String account);

    List<QuestionnairePaperVo> getAllQuestionnaire();

    void chooseQuestion(List<QuestionnaireSelectVo> answers, String questionnaireNum,String account);

    List<QuestionnairePaperVo> getUserQuestion(String account);

    void startAnswerQuestion(String questionnaireNum, String account);
}
