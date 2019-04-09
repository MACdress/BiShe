package com.bishe.portal.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.Encryption;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author:GaoPan
 * @Date:2018/12/19 22:11
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    TbUsersDao tbUsersDao;

    @Override
    public boolean login(String userName, String userPwd,int permission) {
        //表示用户名为空
        if (userName == null) {
            return false;
        }
        TbUsers tbUsers = tbUsersDao.selectUserInfo(userName,permission);
        //表示用户不存在
        if (tbUsers == null) {
            return false;
        }
        String newPwd = Encryption.getPwd(tbUsers.getSale(), userPwd);
        return newPwd.equals(tbUsers.getPwd());
    }

    @Override
    public boolean enroll(TbUsersPo tbUsersPo) {
        boolean registerSuccess = true;
        String newPwd = "";
        String sale = "";
        String pwd = tbUsersPo.getPwd();
        if (pwd != null) {
            sale = Encryption.getSale();
            newPwd = Encryption.getPwd(sale, pwd);
        }
        tbUsersPo.setPwd(newPwd);
        tbUsersPo.setSale(sale);
        TbUsers tbUsers = getTbUsers(tbUsersPo);
        try {
            if(tbUsersDao.insert(tbUsers)==0){
                registerSuccess = false;
            }
        } catch (Exception e) {
            registerSuccess = false;
            e.printStackTrace();
        }
        return registerSuccess;
    }

    @Override
    public TbUsersPo getByUserTel(String tel) {
        TbUsers userInfo = tbUsersDao.getUserInfoByTel(tel);
        return getTbUserPo(userInfo);
    }

    private TbUsersPo getTbUserPo (TbUsers tbUsers){
        TbUsersPo tbUserPo = new TbUsersPo();
        tbUserPo.setAccountNumber(StringUtils.isEmpty(tbUsers.getAccountNumber()) ? "" : tbUsers.getAccountNumber());
        tbUserPo.setBirthDay(StringUtils.isEmpty(tbUsers.getBirthDay()) ? "" : tbUsers.getBirthDay());
        tbUserPo.setEducation(StringUtils.isEmpty(tbUsers.getEducation()) ? "" : tbUsers.getEducation());
        tbUserPo.setEmail(StringUtils.isEmpty(tbUsers.getEmail()) ? "" : tbUsers.getEmail());
        tbUserPo.setName(StringUtils.isEmpty(tbUsers.getName()) ? "" : tbUsers.getName());
        tbUserPo.setPwd(StringUtils.isEmpty(tbUsers.getPwd()) ? "" : tbUsers.getPwd());
        tbUserPo.setSale(StringUtils.isEmpty(tbUsers.getSale()) ? "" : tbUsers.getSale());
        tbUserPo.setUserPhoto(StringUtils.isEmpty(tbUsers.getUserPhoto()) ? "" : tbUsers.getUserPhoto());
        tbUserPo.setSex(tbUsers.getSex());
        tbUserPo.setTel(StringUtils.isEmpty(tbUsers.getTel()) ? "" : tbUsers.getTel());
        tbUserPo.setPermission(tbUsers.getPermission());
        return tbUserPo;
    }
    private TbUsers getTbUsers(TbUsersPo tbUsersPo) {
        TbUsers tbUsers = new TbUsers();
        tbUsers.setAccountNumber(tbUsersPo.getAccountNumber() == null ? "" : tbUsersPo.getAccountNumber());
        tbUsers.setBirthDay(tbUsersPo.getBirthDay() == null ? "" : tbUsersPo.getBirthDay());
        tbUsers.setEducation(tbUsersPo.getEducation() == null ? "" : tbUsersPo.getEducation());
        tbUsers.setEmail(tbUsersPo.getEmail() == null ? "" : tbUsersPo.getEmail());
        tbUsers.setName(tbUsersPo.getName() == null ? "" : tbUsersPo.getName());
        tbUsers.setPwd(tbUsersPo.getPwd() == null ? "" : tbUsersPo.getPwd());
        tbUsers.setSale(tbUsersPo.getSale() == null ? "" : tbUsersPo.getSale());
        tbUsers.setUserPhoto(tbUsersPo.getUserPhoto() == null ? "" : tbUsersPo.getUserPhoto());
        tbUsers.setSex(tbUsersPo.getSex() == null ? 1 : tbUsersPo.getSex());
        tbUsers.setTel(tbUsersPo.getTel() == null ? "" : tbUsersPo.getTel());
        tbUsers.setPermission(tbUsersPo.getPermission() == null ? 0 : tbUsersPo.getPermission());
        return tbUsers;
    }
}
