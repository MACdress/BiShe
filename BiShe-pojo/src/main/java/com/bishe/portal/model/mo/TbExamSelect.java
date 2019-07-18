package com.bishe.portal.model.mo;

import lombok.Data;

/**
 * @author 73515
 */
@Data
public class TbExamSelect {
    private int id;
    private String subjectId;
    private String examTittle;
    private String examParse;
    private String examAnswer;
    private int score;
    private int examMiddleType;
}
