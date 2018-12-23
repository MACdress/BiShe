package com.bishe.portal.service;


import com.bishe.portal.model.po.TbUsersPo;

/**
 * @author 熊猫
 */
public interface UserService {
    /**
     * 登陆
     *
     * @param userName
     * @param userPwd
     * @return
     */
    boolean login(String userName, String userPwd);

    /**
     * 注册
     *
     * @param tbUsers
     */
    void enroll(TbUsersPo tbUsers);

}
