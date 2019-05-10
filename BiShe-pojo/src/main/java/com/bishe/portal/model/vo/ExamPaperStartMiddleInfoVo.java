package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExamPaperStartMiddleInfoVo implements Serializable {
    private String examPaperNumber;
    private List<ExamJudgeInfoVo> examJudgeInfoVos;
    private List<ExamSelectInfoVo> examSelectInfoVos;
    private String  examPaperName;
    private Integer  examPaperType;
    private String  examPaperTypeName;
    private String  createUnit;
    private Integer examPaperCount;
    private Integer examJudgeScore;
    private Integer examSelectScore;
    private Integer allScore;
    private String  examStartNum;
    private String  beginTime;
    private String  endTime;
    private String  score;
    private String  examineeId;
    private String  examineeName;
    private Integer examTime;
}
