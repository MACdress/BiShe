package com.bishe.portal.service;


import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.model.vo.SelectUserParamVo;
import com.bishe.portal.model.vo.UserInfoVo;
import com.bishe.portal.service.utils.ReturnInfo;
import org.springframework.web.multipart.MultipartFile;

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
    UserInfoVo enroll(TbUsersPo tbUsers);


    UserInfoVo getByUserAccount(String account);

    UserInfoVo getByUserTel(String tel);


    List<UserInfoVo> getAllUserInfo(SelectUserParamVo paramVo);

    String readExcelFile(MultipartFile file);

    void outExcelFile();
}
