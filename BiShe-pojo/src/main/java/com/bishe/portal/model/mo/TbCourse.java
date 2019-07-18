package com.bishe.portal.model.mo;

import lombok.Data;

import java.sql.Date;

/**
 * @Author:GaoPan
 * @Date:2018/12/27 20:37
 * @Version 1.0
 **/
@Data
public class TbCourse {
    private int id;
    private int courseNum;
    private String theme;
    private String name;
    /**
     * 课程类型:0表示必修课;1表示选修课
     */
    private int type;
    /**
     * 内容类型：0表示文本课程，1表示视频课程
     */
    private int contentType;
    /**
     * 失效时间
     */
    private Date failureTime;
    /**
     * 是否发布:0表示未发布，1表示发布
     */
    private int onSale;

    private String createUser;

    private String updateUser;
}
