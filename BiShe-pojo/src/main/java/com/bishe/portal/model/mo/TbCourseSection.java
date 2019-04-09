package com.bishe.portal.model.mo;

import lombok.Data;

/**
 * @Author:GaoPan
 * @Date:2018/12/27 20:55
 * @Version 1.0
 **/
@Data
public class TbCourseSection {
    private int id;
    private int courseNum;
    private int parentId;
    private String name;
    /**
     * 时长
     */
    private String time;
    /**
     * 是否上架:0代表未上架，1代表上架
     */
    private int onSale;
    private String videoUrl;
    private String createUser;
    private String updateUser;
}
