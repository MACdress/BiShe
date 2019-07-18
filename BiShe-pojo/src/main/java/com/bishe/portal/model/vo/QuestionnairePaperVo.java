package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionnairePaperVo implements Serializable {
    private String questionnaireName;
    private String questionnaireNum;
    private List<QuestionnaireSelectVo> selectList;
    private String createTime;
}
