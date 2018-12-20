package com.bishe.portal.service.impl;

import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.Encryption;

/**
 * @Author:GaoPan
 * @Date:2018/12/19 22:11
 * @Version 1.0
 **/
public class UserServiceImpl implements UserService {
    @Override
    public TbUsersPo login(String userTel, String userPwd) {
        return null;
    }

    @Override
    public void enroll(TbUsersPo tbUsersPo) {
        String newPwd = "";
        String sale = "";
        String pwd = tbUsersPo.getPwd();
        if(pwd!=null){
            sale = Encryption.getSale();
            newPwd =Encryption.getPwd(sale,pwd);
        }
        tbUsersPo.setPwd(newPwd);
        tbUsersPo.setSale(sale);
        TbUsers tbUsers1 = getTbUsers(tbUsersPo);
    }

    private TbUsers getTbUsers(TbUsersPo tbUsersPo) {
        TbUsers tbUsers = new TbUsers();
        tbUsers.setBirth(tbUsersPo.getBirth());
        tbUsers.setEducation(tbUsersPo.getEducation()==null?"":tbUsersPo.getEducation());
        tbUsers.setEmail(tbUsersPo.getEmail()==null?"":tbUsersPo.getEmail());
        tbUsers.setName(tbUsersPo.getName()==null?"":tbUsersPo.getName());
        tbUsers.setPwd(tbUsersPo.getPwd()==null?"":tbUsersPo.getPwd());
        tbUsers.setSale(tbUsersPo.getSale()==null?"":tbUsersPo.getSale());
        tbUsers.setUserPhoto(tbUsersPo.getUserPhoto()==null?"":tbUsersPo.getUserPhoto());
        tbUsers.setSex(tbUsersPo.getSex()==null?1:tbUsersPo.getSex().intValue());
        tbUsers.setTel(tbUsersPo.getTel()==null?"":tbUsersPo.getTel());
        tbUsers.setPermission(tbUsersPo.getPermission()==null?0:tbUsersPo.getPermission());
        return tbUsers;
    }
}
