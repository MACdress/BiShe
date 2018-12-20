package com.bishe.portal.service;


import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.TbUsersPo;

/**
 * @author 熊猫
 */
public interface UserService {
    /**
     * 登陆
     *
     * @param userTel
     * @param userPwd
     * @return
     */
    TbUsersPo login(String userTel, String userPwd);

    /**
     * 注册
     *
     * @param tbUsers
     */
    void enroll(TbUsersPo tbUsers);

}
