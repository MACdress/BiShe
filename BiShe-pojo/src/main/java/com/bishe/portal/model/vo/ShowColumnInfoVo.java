package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShowColumnInfoVo implements Serializable {
    private Integer columnManageId;
    private String columnManageName;
    private List<ColumnInfoSimpleVo> columnInfoList;
}
