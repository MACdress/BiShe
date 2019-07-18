package com.bishe.portal.model.mo;

import lombok.Data;

import java.util.Date;

@Data
public class TbExamStart {
    private int id;
    private String examStartNum;
    private String examinee;
    private int score;
    private String examPaperNum;
    private int  examEnd;
    private Date createAt;
    private Date updateAt;
}
