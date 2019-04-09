package com.bishe.portal.web.controller;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.SessionContext;
import com.bishe.portal.web.utils.Encryption;
import com.bishe.portal.web.utils.JsonView;
import com.bishe.portal.web.vo.AuthUser;
import com.bishe.portal.web.vo.RegisterUserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    @RequestMapping(value = "/login")
    @ResponseBody
    public String login(AuthUser user, String identiryCode, HttpServletRequest request){

        //如果已经登录过
        if(SessionContext.getAuthUser() != null){
            return JsonView.render(301,"已经登录过");
        }

        //验证码判断
        if(identiryCode!=null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))){
           return JsonView.render(401,"验证码验证失败");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(user.getTel(), Encryption.encodedByMD5(user.getPassword()));
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            return JsonView.render(200,"登录成功");
        }catch(AuthenticationException e){
            return JsonView.render(404,"登录失败");
        }
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
}
