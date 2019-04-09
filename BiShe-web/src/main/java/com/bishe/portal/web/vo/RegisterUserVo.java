package com.bishe.portal.web.vo;

import lombok.Data;

@Data
public class RegisterUserVo {
    private String password;
    private Integer permission;
    private String email;
    private String name;
    private String birthDay;
    private Integer sex;
    private String tel;
}
