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
    boolean login(String userName, String userPwd,int permission);

    /**
     * 注册
     *
     * @param tbUsers
     */
    boolean enroll(TbUsersPo tbUsers);


    TbUsersPo getByUserTel(String tel);
}
