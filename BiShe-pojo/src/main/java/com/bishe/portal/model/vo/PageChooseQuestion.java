package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageChooseQuestion implements Serializable {
    private String questionnaireNum;
    private List<QuestionnaireSelectVo> answers;
}
