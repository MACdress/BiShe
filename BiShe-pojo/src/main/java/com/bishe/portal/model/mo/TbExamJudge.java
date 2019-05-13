package com.bishe.portal.model.mo;

import lombok.Data;

/**
 * @author 73515
 */
@Data
public class TbExamJudge {
    private int id;
    private String subjectId;
    private String examTittle;
    private int examAnswer;
    private int score;
    private int examMiddleType;
}
