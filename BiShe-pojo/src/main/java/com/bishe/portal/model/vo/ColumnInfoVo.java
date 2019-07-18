package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaopan31
 */
@Data
public class ColumnInfoVo implements Serializable {
    private Integer id;
    private String  columnName;
    private Integer parentId;
    private String parentName;
    private String createUserId;
    private String createUserName;
    private String createTime;
    private String columnId;
    private String reviewUserId;
    private String reviewUserName;
    /**
     * 表示是否审核
     * 1：审核
     * 0：不审核
     */
    private Integer isReview;
    /**
     * 1：表示启用
     * 0:表示停用
     */
    private Integer columnStatus;
    private String  columnImg;
    private String  columnText;
    private Integer total;
}
