package com.bishe.portal.model.mo;

import lombok.Data;

import java.util.Date;

@Data
public class TbQuestionnaire {
    private int id;
    private String questionnaireName;
    private String questionnaireNum;
    private Date createAt;
}
