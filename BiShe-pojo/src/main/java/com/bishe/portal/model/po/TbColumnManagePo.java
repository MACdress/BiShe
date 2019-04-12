package com.bishe.portal.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author gaopan31
 */
@Data
public class TbColumnManagePo {
    private String columnId;
    private String columnName;
    private int isReview;
    private String parentId;
    private int columnStatus;
    private String columnImg;
    private String columnText;
    private int reviewUserId;
    private int createUserId;
    private String createUser;
    private String reviewUser;
    private String parentName;
    private Date createAt;
}
