package com.bishe.portal.web.vo;

import com.bishe.portal.web.interfaces.SessionUser;
import lombok.Data;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class AuthUser implements SessionUser {
    /**
     *登录用户名
     **/
    private String realname;

    private String tel;

    /**
     *真实姓名
     **/
    private String username;

    /**
     *密码
     **/
    private String password;

    /**
     *性别
     **/
    private Integer gender;

    /**
     *头像
     **/
    private String header;

    /**
     *手机号码
     **/
    private String mobile;

    /**
     *状态：待审核（0），审核通过（1），默认（2），审核未通过（3），禁用（5）
     **/
    private Integer status;

    /**
     *生日
     **/
    private Date birthday;

    /**
     *签名
     **/
    private String sign;

    /**
     *最后一次登录时间
     **/
    private Date loginTime;

    /**
     *最后一次登录IP
     **/
    private String ip;

    /**
     *所在省份
     **/
    private String province;

    /**
     *所在城市
     **/
    private String city;

    private int permission;

    @Override
    public Set<String> getPermission(){
        return new HashSet<>(S);
    }
}
