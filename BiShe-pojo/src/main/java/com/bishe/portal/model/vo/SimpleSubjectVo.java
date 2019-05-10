package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleSubjectVo implements Serializable {
    private String subjectId;
    /**
     * 1代表选择题
     * 2代表判断题
     */
    private Integer examSubjectType;
}
