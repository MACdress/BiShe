package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ManageColumnInfoSimpleVo implements Serializable {
    private Integer id;
    private Integer belongColumn;
    private Integer informationStatus;
    private String tittle;
    private String createUser;
    private String createUserName;
    private String createAt;
    private String belongColumnName;
}
