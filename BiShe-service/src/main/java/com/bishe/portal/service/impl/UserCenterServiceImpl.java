package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.service.UserCenterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author:GaoPan
 * @Date:2018/12/20 21:44
 * @Version 1.0
 **/
@Service
public class UserCenterServiceImpl implements UserCenterService {
    @Resource
    private TbUsersDao tbUsersDao;

    @Override
    public String updateUserIcon(String userName) {
        return null;
    }

    @Override
    public int changeUserMessage() {
        return 0;
    }

    @Override
    public int getUserInfo(String tel, int permission) {
        tbUsersDao.selectUserInfo(tel,permission);
        return 0;
    }
}
