package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamChooseVo implements Serializable {
    private String examStartNum;
    private String subjectId;
    private String answer;
    private Integer examSubjectType;
}
