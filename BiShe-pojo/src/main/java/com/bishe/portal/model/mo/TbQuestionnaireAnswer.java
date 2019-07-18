package com.bishe.portal.model.mo;

import lombok.Data;

@Data
public class TbQuestionnaireAnswer {
    private int id;
    private String questionnaireNum;
    private String questionnaireId;
    private String questionnaireAnswer;
    private String account;
}
