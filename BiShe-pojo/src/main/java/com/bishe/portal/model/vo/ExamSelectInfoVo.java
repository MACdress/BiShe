package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExamSelectInfoVo implements Serializable {
    private String subjectId;
    private String examTittle;
    private String examParse;
    private String examAnswer;
    private List<ExamSelectOptionInfoVo> selectOptionInfos;
    private Integer score;
    /**
     * 1代表选择提，2代表判断题
     */
    private Integer examSubjectType;
    private Integer examMiddleType;
    private String  examMiddleTypeName;
}
