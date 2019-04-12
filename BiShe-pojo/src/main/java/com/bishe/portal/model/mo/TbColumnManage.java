package com.bishe.portal.model.mo;

import lombok.Data;

/**
 * @author gaopan31
 */
@Data
public class TbColumnManage {
    private int id;
    private String columnId;
    private String columnName;
    private int isReview;
    private int parent;
    private int columnStatus;
    private String columnImg;
    private String columnText;
    private int reviewUser;
    private int createUser;
}
