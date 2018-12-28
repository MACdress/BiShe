package com.bishe.portal.model.mo;

import lombok.Data;

/**
 * @Author:GaoPan
 * @Date:2018/12/27 21:11
 * @Version 1.0
 **/
@Data
public class TbUserCourseSection {
    private int id;
    private int userNum;
    private int courseNum;
    private int sectionNum;
    /**
     * 学习状态：0代表学习中，1代表学习状态
     */
    private int status;
    private String createUser;
    private String updateUser;
}
