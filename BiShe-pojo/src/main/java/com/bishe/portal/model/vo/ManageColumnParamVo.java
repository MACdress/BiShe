package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaopan31
 */
@Data
public class ManageColumnParamVo implements Serializable {
    private String columnName;
    private String tittle;
    private String createTime1;
    private String createTime2;
    private Integer columnStatus;
    private Integer createUserId;
}
