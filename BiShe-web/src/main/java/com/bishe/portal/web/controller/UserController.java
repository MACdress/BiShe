package com.bishe.portal.web.controller;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.model.vo.RegisterUserVo;
import com.bishe.portal.model.vo.SelectUserParamVo;
import com.bishe.portal.model.vo.UserInfoVo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.ReturnInfo;
import com.bishe.portal.web.utils.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody RegisterUserVo registerUserVo, HttpSession httpSession){
        TbUsersPo user = new TbUsersPo();
        user.setTel(registerUserVo.getTel());
        user.setPwd(registerUserVo.getPassword());
        ReturnInfo retuenInfo = userService.login(user);
        if ( retuenInfo.isSuccess()) {
           httpSession.setAttribute("user",retuenInfo.getData());
        }
        return JsonView.render(200,retuenInfo.getMessage(),retuenInfo.getData());
    }

    /**
     * 注册
     * @return 返回响应
     */
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody RegisterUserVo registerUserVo,HttpSession httpSession){
        System.out.println("开始注册");
        UserInfoVo tbUsersPo1;
        UserInfoVo tbUsersPo  = (UserInfoVo) httpSession.getAttribute("user");
        if((tbUsersPo!=null)&&(tbUsersPo.getTel().equals(registerUserVo.getTel()))){
            return JsonView.render(301,"is login");
        }
        tbUsersPo = userService.getByUserTel(registerUserVo.getTel());
        if(tbUsersPo != null){
            return JsonView.render(201,"is register");
        }else{
            tbUsersPo1 = userService.enroll(getUserPo(registerUserVo));
            if (tbUsersPo1 == null){
                return JsonView.render(404,"register error");
            }
        }
        return JsonView.render(200,"register success",tbUsersPo1);
    }

    private TbUsersPo getUserPo(RegisterUserVo registerUserVo) {
        TbUsersPo tbUserPo = new TbUsersPo();
        tbUserPo.setBirthDay(StringUtils.isEmpty(registerUserVo.getBirthDay()) ? "" : registerUserVo.getBirthDay());
        tbUserPo.setEmail(StringUtils.isEmpty(registerUserVo.getEmail()) ? "" : registerUserVo.getEmail());
        tbUserPo.setName(StringUtils.isEmpty(registerUserVo.getName()) ? "" : registerUserVo.getName());
        tbUserPo.setSex(registerUserVo.getSex());
        tbUserPo.setPwd(StringUtils.isEmpty(registerUserVo.getPassword())?"":registerUserVo.getPassword());
        tbUserPo.setTel(StringUtils.isEmpty(registerUserVo.getTel()) ? "" : registerUserVo.getTel());
        tbUserPo.setPermission(registerUserVo.getPermission());
        return tbUserPo;
    }


    @RequestMapping(value = "allUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public String getAllUserInfo (@RequestBody SelectUserParamVo param){
        List<UserInfoVo> userInfoList =  userService.getAllUserInfo(param);
        return JsonView.render(200,"成功",userInfoList);
    }

    @RequestMapping(value = "test",method = RequestMethod.GET)
    @ResponseBody
    public String getTest (){
        return JsonView.render(404,"未登录，请先登录");
    }

    @RequestMapping(value="inputUserInfo",method= {RequestMethod.POST})
    @ResponseBody
    public String inputUserInfo(@RequestParam(value="file_excel") MultipartFile file) {
        String readResult = "";
        try {
            readResult = userService.readExcelFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  JsonView.render(200,readResult);

    }

    @RequestMapping(value="outPutUserInfo",method= {RequestMethod.POST})
    @ResponseBody
    public String outPutUserInfo(){
        userService.outExcelFile();
        return JsonView.render(200,"导出成功");
    }
}
