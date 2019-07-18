package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamSelectOptionInfoVo implements Serializable {
    private Integer id;
    private String option;
    private String optionContent;
}
