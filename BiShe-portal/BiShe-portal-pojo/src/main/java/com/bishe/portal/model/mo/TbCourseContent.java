package com.bishe.portal.model.mo;

import lombok.Data;

/**
 * @Author:GaoPan
 * @Date:2018/12/27 20:51
 * @Version 1.0
 **/
@Data
public class TbCourseContent {
    private int id;
    private int userNumber;
    private int toUserNumber;
    private int courseNum;
    private int sectionNum;
    private String sectionTittle;
    private String content;
    /**
     * 引用id
     */
    private int refId;
    /**
     * 引用内容
     */
    private String refContent;
    /**
     * 评论类型:0代表评论，1代表答疑
     */
    private int type;
    private String createUser;
    private String updateUser;
}
