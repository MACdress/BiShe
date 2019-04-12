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
}
