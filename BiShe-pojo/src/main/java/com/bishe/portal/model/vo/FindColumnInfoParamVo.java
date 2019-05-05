package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FindColumnInfoParamVo implements Serializable {
    private String tittle;
    private Integer belongColumn;
    private String createBeginTime;
    private String createEndTime;
    private Integer informationStatus;
    private String createUser;
}
