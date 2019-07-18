package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AddExamPaperMiddleVo implements Serializable {
    private String examPaperNum;
    List<SimpleSubjectVo> subjectList;
}
