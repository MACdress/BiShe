package com.bishe.portal.model.mo;

import lombok.Data;

@Data
public class TbExamChoose {
    private int id;
    private String subjectId;
    private String answer;
    private int isCorrect;
    private String examStartNum;
    private Integer examSubjectType;
}
