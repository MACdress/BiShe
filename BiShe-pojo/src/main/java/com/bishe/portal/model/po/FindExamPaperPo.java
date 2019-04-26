package com.bishe.portal.model.po;

import lombok.Data;

/**
 * @author 73515
 */
@Data
public class FindExamPaperPo {
    private String examPaperName;
    private String invalidBeginTime;
    private String invalidEndTime;
    private String createUnit;
    private String responsible;
    private Integer status;
}