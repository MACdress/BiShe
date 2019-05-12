package com.bishe.portal.model.mo;

import lombok.Data;

import java.util.Date;


/**
 * @author 73515
 */
@Data
public class TbExamPaper {
    private int id;
    private String examPaperName;
    private Integer examPaperType;
    /**
     * 失效时间
     */
    private String invalidTime;
    /**
     * 主办单位
     */
    private String createUnit;
    /**
     * 责任人
     */
    private String responsible;
    /**
     * 试卷状态：1代表以发布，2代表未发布
     */
    private int status;
    private Date updateAt;
    /**
     * 试卷编号
     */
    private String examPaperNum;

    private int examJudgeScore;

    private int examSelectScore;

    private int examPaperCount;

    private int examTime;
    private String examPaperImg;
}
