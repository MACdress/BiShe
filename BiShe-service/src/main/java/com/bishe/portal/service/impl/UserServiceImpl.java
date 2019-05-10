package com.bishe.portal.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.SimpleUserInfo;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.model.vo.SelectUserParamVo;
import com.bishe.portal.model.vo.UserInfoVo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.Encryption;
import com.bishe.portal.service.utils.ExcelUtils;
import com.bishe.portal.service.utils.ReturnInfo;
import com.bishe.portal.service.utils.UUIDUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        TbUsers userInfo = tbUsersDao.getUserInfoByTel(user.getTel());
        if (userInfo != null) {
            String newPwd = Encryption.getPwd(userInfo.getSale(), user.getPwd());
            if (newPwd.equals(userInfo.getPwd())) {
                System.out.println("login success");
                returnInfo.setSuccess(true);
                returnInfo.setMessage("login success");
                userInfo.setSale("");
                returnInfo.setData(getUserInfoVo(userInfo));
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
    public UserInfoVo enroll(TbUsersPo tbUsersPo) {
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
        tbUsers.setPwd(tbUsersPo.getPwd()==null?"":tbUsersPo.getPwd());
        tbUsers.setSale(tbUsersPo.getSale());
        tbUsersDao.insert(tbUsers);
        return getUserInfoVo(tbUsersDao.getUserInfoByAccount(tbUsers.getAccount()));
    }

    @Override
    public UserInfoVo getByUserAccount(String account) {
        TbUsers userInfo = tbUsersDao.getUserInfoByAccount(account);
        return getUserInfoVo(userInfo);
    }

    @Override
    public UserInfoVo getByUserTel(String tel) {
        TbUsers userInfo = tbUsersDao.getUserInfoByTel(tel);
        return getUserInfoVo(userInfo);
    }

    @Override
    public List<UserInfoVo> getAllUserInfo(SelectUserParamVo paramVo) {
        List<TbUsers> userInfo = tbUsersDao.getAllUserInfo(paramVo);
        List<UserInfoVo> result  = new ArrayList<>();
        for (TbUsers tbUsers:userInfo){
            result.add(getUserInfoVo(tbUsers));
        }
        return result;
    }

    @Override
    public String readExcelFile(MultipartFile file) {
        ExcelUtils excelUtils = new ExcelUtils();
        List <TbUsers> insertParam ;
        try {
            insertParam = excelUtils.getExcelInfo(file);
        }catch (Exception e){
            System.out.println("导入Excel文件出现错误");
            return  "导入失败";
        }
        if (insertParam!=null && insertParam.size()>0){
            for (TbUsers tbUsers : insertParam) {
                tbUsersDao.insert(tbUsers);
            }
        }
        return "导入成功";
    }

    @Override
    public void outExcelFile() {

    }

    private TbUsersPo getTbUserPo (TbUsers tbUsers){
        TbUsersPo tbUserPo = new TbUsersPo();
        if (tbUsers == null){
            return tbUserPo;
        }
        tbUserPo.setBirthDay(StringUtils.isEmpty(tbUsers.getBirthDay()) ? "" : tbUsers.getBirthDay());
        tbUserPo.setIdCard(StringUtils.isEmpty(tbUsers.getIdCard()) ? "" : tbUsers.getIdCard());
        tbUserPo.setSex(tbUsers.getSex());
        tbUserPo.setTel(StringUtils.isEmpty(tbUsers.getTel()) ? "" : tbUsers.getTel());
        tbUserPo.setPermission(tbUsers.getPermission());
        tbUserPo.setEmail(StringUtils.isEmpty(tbUsers.getEmail()) ? "" : tbUsers.getEmail());
        tbUserPo.setAccount(tbUsers.getAccount());
        tbUserPo.setJob(StringUtils.isEmpty(tbUsers.getJob()) ? "" : tbUsers.getJob());
        tbUserPo.setFixedTel(StringUtils.isEmpty(tbUsers.getFixedTel()) ? "" : tbUsers.getFixedTel());
        tbUserPo.setName(StringUtils.isEmpty(tbUsers.getName()) ? "" : tbUsers.getName());
        tbUserPo.setIdentity(tbUsers.getIdentity());
        tbUserPo.setTurnPositiveDate(StringUtils.isEmpty(tbUsers.getTurnPositiveDate()) ? "" : tbUsers.getTurnPositiveDate());
        tbUserPo.setNationality(StringUtils.isEmpty(tbUsers.getNationality()) ? "" : tbUsers.getNationality());
        tbUserPo.setBranch(StringUtils.isEmpty(tbUsers.getBranch()) ? "" : tbUsers.getBranch());
        tbUserPo.setJoinPartyDate(StringUtils.isEmpty(tbUsers.getJoinPartyDate()) ? "" : tbUsers.getJoinPartyDate());
        tbUserPo.setAddress(StringUtils.isEmpty(tbUsers.getAddress()) ? "" : tbUsers.getAddress());
        return tbUserPo;
    }
    private TbUsers getTbUsers(TbUsersPo tbUsersPo) {
        TbUsers tbUsers = new TbUsers();
        if (tbUsersPo == null){
            return tbUsers;
        }
        tbUsers.setBirthDay(StringUtils.isEmpty(tbUsersPo.getBirthDay()) ? "" : tbUsersPo.getBirthDay());
        tbUsers.setIdCard(StringUtils.isEmpty(tbUsersPo.getIdCard()) ? "" : tbUsersPo.getIdCard());
        tbUsers.setSex(tbUsersPo.getSex());
        tbUsers.setTel(StringUtils.isEmpty(tbUsersPo.getTel()) ? "" : tbUsersPo.getTel());
        tbUsers.setPermission(tbUsersPo.getPermission());
        tbUsers.setEmail(StringUtils.isEmpty(tbUsersPo.getEmail()) ? "" : tbUsersPo.getEmail());
        tbUsers.setAccount(UUIDUtils.getUUID(8));
        tbUsers.setJob(StringUtils.isEmpty(tbUsersPo.getJob()) ? "" : tbUsersPo.getJob());
        tbUsers.setFixedTel(StringUtils.isEmpty(tbUsersPo.getFixedTel()) ? "" : tbUsersPo.getFixedTel());
        tbUsers.setName(StringUtils.isEmpty(tbUsersPo.getName()) ? "" : tbUsersPo.getName());
        tbUsers.setIdentity(tbUsersPo.getIdentity()==null?0:tbUsersPo.getIdentity());
        tbUsers.setTurnPositiveDate(StringUtils.isEmpty(tbUsersPo.getTurnPositiveDate()) ? "" : tbUsersPo.getTurnPositiveDate());
        tbUsers.setNationality(StringUtils.isEmpty(tbUsersPo.getNationality()) ? "" : tbUsersPo.getNationality());
        tbUsers.setBranch(StringUtils.isEmpty(tbUsersPo.getBranch()) ? "" : tbUsersPo.getBranch());
        tbUsers.setJoinPartyDate(StringUtils.isEmpty(tbUsersPo.getJoinPartyDate()) ? "" : tbUsersPo.getJoinPartyDate());
        tbUsers.setAddress(StringUtils.isEmpty(tbUsersPo.getAddress()) ? "" : tbUsersPo.getAddress());
        return tbUsers;
    }

    private UserInfoVo getUserInfoVo (TbUsers tbUsers){
        UserInfoVo result = new UserInfoVo();
        if(tbUsers == null){
            return null;
        }
        result.setBirthDay(StringUtils.isEmpty(tbUsers.getBirthDay()) ? "" : tbUsers.getBirthDay());
        result.setSex(tbUsers.getSex());
        result.setTel(StringUtils.isEmpty(tbUsers.getTel()) ? "" : tbUsers.getTel());
        result.setPermission(tbUsers.getPermission());
        result.setEmail(StringUtils.isEmpty(tbUsers.getEmail()) ? "" : tbUsers.getEmail());
        result.setAccount(StringUtils.isEmpty(tbUsers.getAccount()) ? "" : tbUsers.getAccount());
        result.setName(StringUtils.isEmpty(tbUsers.getName()) ? "" : tbUsers.getName());
        result.setIdCard(StringUtils.isEmpty(tbUsers.getIdCard()) ? "" : tbUsers.getIdCard());
        result.setIdentity(tbUsers.getIdentity());
        result.setNationality(StringUtils.isEmpty(tbUsers.getNationality()) ? "" : tbUsers.getNationality());
        result.setBranch(StringUtils.isEmpty(tbUsers.getBranch()) ? "" : tbUsers.getBranch());
        result.setFixedTel(StringUtils.isEmpty(tbUsers.getFixedTel()) ? "" : tbUsers.getFixedTel());
        result.setAddress(StringUtils.isEmpty(tbUsers.getAddress()) ? "" : tbUsers.getAddress());
        result.setJob(StringUtils.isEmpty(tbUsers.getJob()) ? "" : tbUsers.getJob());
        result.setJoinPartyDate(StringUtils.isEmpty(tbUsers.getJoinPartyDate()) ? "" : tbUsers.getJoinPartyDate());
        result.setTurnPositiveDate(StringUtils.isEmpty(tbUsers.getTurnPositiveDate()) ? "" : tbUsers.getTurnPositiveDate());
        return result;
    }
}
