package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamJudgeInfoVo implements Serializable {
    private String subjectId;
    private String examTittle;
    private String examAnswer;
    private Integer score;
    private String examPaperNum;
    /**
     * 1代表选择提，2代表判断题
     */
    private Integer examSubjectType;
    private Integer examMiddleType;
    private String  examMiddleTypeName;
}
