package com.bishe.portal.service;


import com.bishe.portal.model.po.SimpleUserInfo;
import com.bishe.portal.model.po.TbUsersPo;

import java.beans.SimpleBeanInfo;
import java.util.List;

/**
 * @author 熊猫
 */
public interface UserService {
    /**
     * 登陆
     *
     * @param user
     * @return
     */
    boolean login(TbUsersPo user);

    /**
     * 注册
     *
     * @param tbUsers
     */
    boolean enroll(TbUsersPo tbUsers);


    TbUsersPo getByUserTel(String tel);

    List<SimpleUserInfo> getAllAdminUserInfo();
}
