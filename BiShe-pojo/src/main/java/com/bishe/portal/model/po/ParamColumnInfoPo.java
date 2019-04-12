package com.bishe.portal.model.po;

import lombok.Data;

/**
 * @author gaopan31
 */
@Data
public class ParamColumnInfoPo {
    private Integer parentId;
    private Integer createUserId;
    private String columnId;
    private String columnName;
    private Integer reviewUserId;
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
    private String  tittle;
}
