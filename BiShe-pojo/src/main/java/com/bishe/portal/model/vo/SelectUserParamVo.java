package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectUserParamVo implements Serializable {
    private Integer identity;
    private String name;
    private Integer sex;
    private String idCard;
    private String joinPartyDate;
    private String branch;
    private Integer page;
    private Integer pageSize;
}
