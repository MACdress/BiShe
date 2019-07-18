package com.bishe.portal.model.mo;

import lombok.Data;

import java.util.Date;

@Data
public class TbColumnInfo {
    private int id;
    private String tittle;
    private String origin;
    private String author;
    private String originUrl;
    private Integer belongColumn;
    /**
     * 0代表不能评论，1代表可以评论
     */
    private Integer isComment;
    private String downTime;
    private String commentBeginTime;
    private String commentEndTime;
    private String summary;
    private String text;
    private String infoImg;
    /**
     * 0表示草稿，1表示已发布
     */
    private Integer informationStatus;
    private String createUser;
    private Integer isTop;
    private Date createAt;
}
