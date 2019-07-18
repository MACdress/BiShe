package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageChooseExamVo implements Serializable {
    private String examStartNum;
    private List<ExamChooseVo> examChooseList;
}
