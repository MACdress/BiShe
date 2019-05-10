package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 73515
 */
@Data
public class FindExamPaperVo implements Serializable {
    private String examPaperNum;
    private String examPaperName;
    private String invalidBeginTime;
    private String invalidEndTime;
    private String createUnit;
    private String responsible;
    private Integer status;
    private Integer page;
    private Integer pageSize;
    private Integer examPaperType;
}
