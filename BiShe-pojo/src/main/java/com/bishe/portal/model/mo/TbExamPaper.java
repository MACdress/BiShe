package com.bishe.portal.model.mo;

import lombok.Data;


/**
 * @author 73515
 */
@Data
public class TbExamPaper {
    private int id;
    private String examPaperName;
    private String examPaperType;
    private String invalidTime;
    private String createUnit;
    private String responsible;
    private int status;
}
