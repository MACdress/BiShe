package com.bishe.portal.service;

/**
 * @Author:GaoPan
 * @Date:2018/12/20 21:44
 * @Version 1.0
 **/
public interface UserCenterService {
    /**
     * 更新用户头像
     * @param userName
     * @return
     */
    String updateUserIcon(String userName);

    /**
     * 更新用户信息
     * @return
     */
    int changeUserMessage();

    int getUserInfo(String tel,int permission);
}
