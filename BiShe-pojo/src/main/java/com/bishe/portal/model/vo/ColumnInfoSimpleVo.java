package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ColumnInfoSimpleVo implements Serializable {
    private Integer id;
    private Integer belongColumn;
    private String  summary;
    private String  infoImg;
    private Integer isTop;
}
