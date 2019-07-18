package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionnaireSelectVo implements Serializable {
    private String question;
    private String questionId;
    private String answer;
    private String questionnaireNum;
    private List<ExamSelectOptionInfoVo> options;
}
