package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaopan31
 */
@Data
public class ManageInformationParamVo implements Serializable {
    private String tittle;
    private String author;
    private String edit;
    private String key;
    private String origin;
    private String originUrl;
    private Integer belongColumn;
    private Integer informationStatus;
    private String downTime;
    private Integer isComment;
    private String commentBeginTime;
    private String commentEndTime;
    private String summary;
    private String text;
}
