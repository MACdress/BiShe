package com.bishe.portal.model.mo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 熊猫
 */
@Data
@NoArgsConstructor
public class TbUsers {
    private int id;
    private String pwd;
    private String email;
    private String name;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 人员类别:1代表预备党员，2代表正式党员
     */
    private int identity;
    /**
     * 用戶權限：0代表普通用戶，1代表管理者
     */
    private int permission;
    /**
     * 账号
     */
    private String account;
    private String tel;
    private String birthDay;
    /**
     * 0代表女，1代表男
     */
    private int sex;
    /**
     * 盐值
     */
    private String sale;
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
    private String userImg;
    private String monthlySalary;
    private int basePay;
}
