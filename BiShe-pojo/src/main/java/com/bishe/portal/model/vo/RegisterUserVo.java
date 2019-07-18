package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaopan31
 */
@Data
public class RegisterUserVo implements Serializable {
    private String password;
    private Integer permission;
    private String email;
    private String name;
    private String birthDay;
    private Integer sex;
    private String tel;
    private String address;
    private String branch;
    private String fixedTel;
    private String idCard;
    private Integer identity;
    private String job;
    private String joinPartyDate;
    private String nationality;
    private String turnPositiveDate;
}
