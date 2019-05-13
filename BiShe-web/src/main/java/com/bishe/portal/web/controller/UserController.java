package com.bishe.portal.web.controller;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.model.vo.RegisterUserVo;
import com.bishe.portal.model.vo.SelectUserParamVo;
import com.bishe.portal.model.vo.UserInfoVo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.ReturnInfo;
import com.bishe.portal.web.utils.JsonView;
import com.bishe.portal.web.utils.QRUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.util.Hashtable;
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
    public String inputUserInfo(@RequestParam(value="file") MultipartFile file) {
        String readResult = "";
        try {
            readResult = userService.readExcelFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  JsonView.render(200,readResult);

    }

    @RequestMapping(value="outPutUserInfo",method= {RequestMethod.GET})
    @ResponseBody
    public String outPutUserInfo(HttpServletResponse httpResponse){
        try {
            HSSFWorkbook hssfWorkbook = userService.outExcelFile();
            System.out.println("------"+hssfWorkbook);
            if (hssfWorkbook!=null) {
                String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
                String headStr = "attachment; filename=\"" + fileName + "\"";
                httpResponse.setContentType("APPLICATION/OCTET-STREAM");
                httpResponse.setHeader("Content-Disposition", headStr);
                OutputStream out = httpResponse.getOutputStream();
                hssfWorkbook.write(out);
                out.flush();
                out.close();
                return JsonView.render(200, "导出成功");
            }else {
                return JsonView.render(404, "导出失败");
            }
        }catch (Exception e){
            System.out.println(e);
            return JsonView.render(404, "导出失败");
        }

    }

    @RequestMapping(value="uploadUserImg",method= {RequestMethod.POST})
    @ResponseBody
    public String uploadUserImg(@RequestParam("file") MultipartFile file,HttpSession session){
//        UserInfoVo tbUsersPo  = (UserInfoVo) session.getAttribute("user");
//        if ((tbUsersPo == null)||(tbUsersPo.getPermission()!=1)){
//            return JsonView.render(404,"user is not admin");
//        }
        String[] strings = userService.uploadUserImg(file,"8cb1e006");
        return JsonView.render(200,"成功",strings);
    }

    @RequestMapping(value="goAlipay",method= {RequestMethod.GET})
    @ResponseBody
    public void goAlipay(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){

    }
    @ResponseBody
    @RequestMapping("/qrcode.do")
    public void qrcode(HttpServletRequest request, HttpServletResponse response,
                       ModelMap modelMap) {
        try {
            String productId = request.getParameter("productId");
            String userId = "user01";
            String text = userService.weixinPay(userId, productId);
            //根据url来生成生成二维码
            int width = 300;
            int height = 300;
            //二维码的图片格式
            String format = "gif";
            Hashtable hints = new Hashtable();
            //内容所使用编码
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix;
            try {
                bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
                QRUtil.writeToStream(bitMatrix, format, response.getOutputStream());
            } catch (WriterException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
        }
    }
    @ResponseBody
    @RequestMapping("/hadPay.do")
    public String hadPay(HttpServletRequest request) {
        try {
            //简单的业务逻辑：在微信的回调接口里面，已经定义了，回调返回成功的话，那么 _PAY_RESULT 不为空
            if(request.getSession().getAttribute("_PAY_RESULT") != null ){
                return JsonView.render(200,"支付成功");
            }
            return JsonView.render(200,"支付失败");
        } catch (Exception e) {
            return JsonView.render(200,e.getMessage());
        }
    }

    @RequestMapping(value = "updateUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public String updateUserInfo(@RequestBody RegisterUserVo registerUserVo,HttpSession session){
        UserInfoVo tbUsersPo  = (UserInfoVo) session.getAttribute("user");
        if((tbUsersPo!=null)&&(tbUsersPo.getTel().equals(registerUserVo.getTel()))){
            return JsonView.render(301,"is login");
        }
        userService.updateUserInfo(getUserPo(registerUserVo),tbUsersPo.getAccount());
        return JsonView.render(200,"更新成功");
    }
}
