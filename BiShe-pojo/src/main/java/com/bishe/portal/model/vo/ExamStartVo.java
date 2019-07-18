package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamStartVo implements Serializable {
    private String examStartNum;
    private String examinee;
    private int score;
    private String examPaperNum;
    private String beginTime;
    private String endTime;
    private int allScore;
    private String examPaperName;
    private String examineeName;
}
