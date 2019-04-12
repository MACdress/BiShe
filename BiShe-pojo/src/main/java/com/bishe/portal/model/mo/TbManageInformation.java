package com.bishe.portal.model.mo;

import lombok.Data;

/**
 * @author gaopan31
 */
@Data
public class TbManageInformation {
    private int id;
    private String tittle;
    private String author;
    private String edit;
    private String key;
    private String origin;
    private String originUrl;
    private int belongColumn;
    private int informationStatus;
    private String downTime;
    private int isComment;
    private String commentBeginTime;
    private String commentEndTime;
    private String summary;
    private String text;
    private int createUser;
}
