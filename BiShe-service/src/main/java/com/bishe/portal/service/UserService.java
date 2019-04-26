package com.bishe.portal.service;


import com.bishe.portal.model.po.SimpleUserInfo;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.service.utils.ReturnInfo;

import java.util.List;

/**
 * @author 熊猫
 */
public interface UserService {
    /**
     * 登陆
     *
     * @param user 用户登录信息
     * @return 提示信息
     */
    ReturnInfo login(TbUsersPo user);

    /**
     * 注册
     *
     * @param tbUsers
     */
    boolean enroll(TbUsersPo tbUsers);


    TbUsersPo getByUserAccount(String account);

    List<SimpleUserInfo> getAllAdminUserInfo();
}
