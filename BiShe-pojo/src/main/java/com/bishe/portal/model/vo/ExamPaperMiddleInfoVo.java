package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExamPaperMiddleInfoVo implements Serializable {
    private String examPaperNumber;
    private List<ExamJudgeInfoVo> examJudgeInfoVos;
    private List<ExamSelectInfoVo> examSelectInfoVos;
    private String  examPaperName;
    private Integer  examPaperType;
    private String  invalidTime;
    private String  createUnit;
    private String  responsible;
    private Integer status;
    private Integer examPaperCount;
    private Integer examJudgeScore;
    private Integer examSelectScore;
    private Integer examPaperScore;
}
