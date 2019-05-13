package com.bishe.portal.model.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVo implements Serializable {
    private String email;
    private String name;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 人员类别:O代表预备党员，1代表正式党员
     */
    private Integer identity;
    /**
     * 用戶權限：0代表普通用戶，1代管理员;
     * */
    private Integer permission;
    /**
     * 账号
     */
    private String account;
    private String tel;
    private String birthDay;
    /**
     * 0代表女，1代表男
     */
    private Integer sex;
    /**
     * 民族
     */
    private String nationality;
    /**
     * 所在党支部
     */
    private String branch;
    /**
     * 固定电话
     */
    private String fixedTel;
    private String address;
    /**
     * 工作岗位
     */
    private String job;
    /**
     * 入党日期
     */
    private String joinPartyDate;
    /**
     /      * 转正日期
     */
    private String turnPositiveDate;
    private String monthlySalary;

}
