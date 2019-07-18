package com.bishe.portal.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author gaopan31
 */
@Data
public class TbColumnManagePo {
    private int id;
    private Integer  parentId;
    private String columnName;
    private int isReview;
    private String parent;
    private int columnStatus;
    private String columnImg;
    private String columnText;
    private String reviewUserId;
    private String  createUserId;
    private String createUser;
    private String reviewUser;
    private String parentName;
    private Date createAt;
}
