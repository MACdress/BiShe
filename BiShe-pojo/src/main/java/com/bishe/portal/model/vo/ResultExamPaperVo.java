package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultExamPaperVo implements Serializable {
    private Integer page;
    private Integer pageSize;
    private Object result;
}
