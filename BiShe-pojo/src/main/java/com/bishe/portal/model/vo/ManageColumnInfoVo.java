package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ManageColumnInfoVo implements Serializable {
    private Integer id;
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
    private String summary;
    private String text;
    /**
     * 0表示草稿，1表示已发布
     */
    private Integer informationStatus;
    private String createUser;
    private Integer isTop;
    private String createUserName;
    private String createAt;
    private String belongColumnName;
}
