package com.bishe.portal.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.SimpleUserInfo;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.Encryption;
import com.bishe.portal.service.utils.ReturnInfo;
import com.bishe.portal.service.utils.UUIDUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public ReturnInfo login(TbUsersPo user) {
        ReturnInfo returnInfo = new ReturnInfo();
        TbUsers userInfo = tbUsersDao.getUserInfoByAccount(user.getAccount());
        if (userInfo != null) {
            String newPwd = Encryption.getPwd(userInfo.getSale(), user.getPwd());
            if (newPwd.equals(userInfo.getPwd())) {
                System.out.println("login success");
                returnInfo.setSuccess(true);
                returnInfo.setMessage("login success");
            } else {
                System.out.println("password error");
                returnInfo.setSuccess(false);
                returnInfo.setMessage("password error");
            }
        } else {
            System.out.println("account not exist");
            returnInfo.setSuccess(false);
            returnInfo.setMessage("account not exist");
        }
        return returnInfo;
    }

    @Override
    public TbUsersPo enroll(TbUsersPo tbUsersPo) {
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
        tbUsersDao.insert(tbUsers);
        return getTbUserPo(tbUsersDao.getUserInfoByAccount(tbUsers.getAccount()));
    }

    @Override
    public TbUsersPo getByUserAccount(String account) {
        TbUsers userInfo = tbUsersDao.getUserInfoByAccount(account);
        return getTbUserPo(userInfo);
    }

    @Override
    public TbUsersPo getByUserTel(String tel) {
        TbUsers userInfo = tbUsersDao.getUserInfoByTel(tel);
        return null;
    }

    @Override
    public List<SimpleUserInfo> getAllAdminUserInfo() {
        return tbUsersDao.getAllAdminUserInfo();
    }

    private TbUsersPo getTbUserPo (TbUsers tbUsers){
        TbUsersPo tbUserPo = new TbUsersPo();
        if (tbUsers == null){
            return tbUserPo;
        }
        tbUserPo.setBirthDay(StringUtils.isEmpty(tbUsers.getBirthDay()) ? "" : tbUsers.getBirthDay());
        tbUserPo.setPwd(StringUtils.isEmpty(tbUsers.getPwd()) ? "" : tbUsers.getPwd());
        tbUserPo.setSale(StringUtils.isEmpty(tbUsers.getSale()) ? "" : tbUsers.getSale());
        tbUserPo.setSex(tbUsers.getSex());
        tbUserPo.setTel(StringUtils.isEmpty(tbUsers.getTel()) ? "" : tbUsers.getTel());
        tbUserPo.setPermission(tbUsers.getPermission());
        tbUserPo.setId(tbUsers.getId());
        tbUserPo.setName(StringUtils.isEmpty(tbUsers.getName()) ? "" : tbUsers.getName());
        tbUserPo.setEmail(StringUtils.isEmpty(tbUsers.getEmail()) ? "" : tbUsers.getEmail());
        return tbUserPo;
    }
    private TbUsers getTbUsers(TbUsersPo tbUsersPo) {
        TbUsers tbUsers = new TbUsers();
        tbUsers.setBirthDay(tbUsersPo.getBirthDay() == null ? "" : tbUsersPo.getBirthDay());
        tbUsers.setEmail(tbUsersPo.getEmail() == null ? "" : tbUsersPo.getEmail());
        tbUsers.setName(tbUsersPo.getName() == null ? "" : tbUsersPo.getName());
        tbUsers.setPwd(tbUsersPo.getPwd() == null ? "" : tbUsersPo.getPwd());
        tbUsers.setSale(tbUsersPo.getSale() == null ? "" : tbUsersPo.getSale());
        tbUsers.setSex(tbUsersPo.getSex() == null ? 1 : tbUsersPo.getSex());
        tbUsers.setTel(tbUsersPo.getTel() == null ? "" : tbUsersPo.getTel());
        tbUsers.setPermission(tbUsersPo.getPermission() == null ? 0 : tbUsersPo.getPermission());
        tbUsers.setAccount(UUIDUtils.getUUID(8));
        return tbUsers;
    }
}
