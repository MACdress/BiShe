package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 73515
 */
@Data
public class ExamPaperVo implements Serializable {
    private String examPaperName;
    private String examPaperType;
    private String invalidTime;
    private String createUnit;
    private String responsible;
    private Integer status;
}
