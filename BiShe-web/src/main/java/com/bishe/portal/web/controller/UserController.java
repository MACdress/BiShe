package com.bishe.portal.web.controller;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.model.po.SimpleUserInfo;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.SessionContext;
import com.bishe.portal.web.utils.JsonView;
import com.bishe.portal.model.vo.RegisterUserVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 熊猫
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/userCenter")
public class UserController {
    @Resource
    private UserService userService;
    /**
     * 登录
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("tel")String tel,@RequestParam("password")String password){
        TbUsersPo user = new TbUsersPo();
        user.setTel(tel);
        user.setPwd(password);
        if (userService.login(user)) {
            return JsonView.render(200,"登录成功");
        }
        return JsonView.render(401,"登录失败");
    }

    /**
     * 注册
     * @return
     */
    @RequestMapping(value = "/register")
    @ResponseBody
    public String register(RegisterUserVo registerUserVo){
        if(SessionContext.isLogin()){
            return JsonView.render(301,"已经登陆过");
        }
        TbUsersPo tbUsersPo = userService.getByUserTel(registerUserVo.getTel());
        if(tbUsersPo != null){
            return JsonView.render(201,"已经注册过");
        }else{
            boolean enroll = userService.enroll(getUserPo(registerUserVo));
            if (!enroll){
                return JsonView.render(404,"注册失败");
            }
        }
        return JsonView.render(200,"注册成功");
    }

    private TbUsersPo getUserPo(RegisterUserVo registerUserVo) {
        TbUsersPo tbUserPo = new TbUsersPo();
        tbUserPo.setBirthDay(StringUtils.isEmpty(registerUserVo.getBirthDay()) ? "" : registerUserVo.getBirthDay());
        tbUserPo.setEmail(StringUtils.isEmpty(registerUserVo.getEmail()) ? "" : registerUserVo.getEmail());
        tbUserPo.setName(StringUtils.isEmpty(registerUserVo.getName()) ? "" : registerUserVo.getName());
        tbUserPo.setSex(registerUserVo.getSex().intValue());
        tbUserPo.setPwd(StringUtils.isEmpty(registerUserVo.getPassword())?"":registerUserVo.getPassword());
        tbUserPo.setTel(StringUtils.isEmpty(registerUserVo.getTel()) ? "" : registerUserVo.getTel());
        tbUserPo.setPermission(registerUserVo.getPermission());
        return tbUserPo;
    }

    @RequestMapping(value = "/adminUserInfo")
    @ResponseBody
    public String getAllAdminName (){
        List<SimpleUserInfo> userInfoList =  userService.getAllAdminUserInfo();
        return JsonView.render(200,"成功",userInfoList);
    }
}
