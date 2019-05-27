package com.bishe.portal.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.bishe.portal.dao.TbUserEventDao;
import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.model.mo.TbUserEvent;
import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.model.vo.SelectUserParamVo;
import com.bishe.portal.model.vo.UserEventVo;
import com.bishe.portal.model.vo.UserInfoVo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom.JDOMException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:GaoPan
 * @Date:2018/12/19 22:11
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    TbUsersDao tbUsersDao;
    @Resource
    TbUserEventDao tbUserEventDao;

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
                System.out.println(userInfo);
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
        tbUsers.setAccount(UUIDUtils.getUUID(8));
        tbUsersDao.insert(tbUsers);
        TbUserEvent event = new TbUserEvent();
        event.setEventDate(new Date());
        event.setAccount(tbUsers.getAccount());
        event.setEvent(tbUsers.getName()+"用户注册账号");
        tbUserEventDao.insertEvent(event);
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
        int page;
        if (paramVo.getPage()<1){
            page = 1;
        }else{
            page = paramVo.getPage();
        }
        page = (page-1)*paramVo.getPageSize();
        paramVo.setPage(page);
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
    public HSSFWorkbook outExcelFile() {
        List<TbUsers> userInfo = tbUsersDao.getAllUserInfoNoPage();
        HSSFWorkbook sheets = ExcelUtils.outPutUserInfoExcel(userInfo);
        return sheets;
    }

    @Override
    public String[] uploadUserImg(MultipartFile file, String account) {
        String[] split = new String[0];
        try {
            COSClientUtil cosClientUtil = new COSClientUtil();
            String name = cosClientUtil.uploadFile2Cos(file);
            String imgUrl = cosClientUtil.getImgUrl(name);
            tbUsersDao.updateUserImg(imgUrl,account);
            split = imgUrl.split("\\?");
        }catch (Exception e){
            e.printStackTrace();
        }
        return split;
    }

    @Override
    public String weixinPay(String userId, String productId) {
        String out_trade_no = "" + System.currentTimeMillis(); //订单号 （调整为自己的生产逻辑）

        // 账号信息
        String appid = AlipayConfig.APP_ID;  // appid
        //String appsecret = PayConfigUtil.APP_SECRET; // appsecret
        String mch_id = AlipayConfig.MCH_ID; // 商业号
        String key = AlipayConfig.API_KEY; // key

        String currTime = PayToolUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayToolUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;

        // 获取发起电脑 ip
        String spbill_create_ip = AlipayConfig.CREATE_IP;
        // 回调接口
        String notify_url = AlipayConfig.NOTIFY_URL;
        String trade_type = "NATIVE";

        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", "可乐");  //（调整为自己的名称）
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", "10"); //价格的单位为分
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);

        String sign = PayToolUtil.createSign("UTF-8", packageParams,key);
        packageParams.put("sign", sign);

        String requestXML = PayToolUtil.getRequestXml(packageParams);
        System.out.println(requestXML);
        String resXml = HttpUtil.postData(AlipayConfig.UFDODER_URL, requestXML);

        Map map = null;
        try {
            try {
                map = XMLUtil4jdom.doXMLParse(resXml);
            } catch (JDOMException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String urlCode = (String) map.get("code_url");

        return urlCode;
    }

    @Override
    public void updateUserInfo(TbUsersPo registerUserVo, String account) {
        TbUsers tbUsers = getTbUsers(registerUserVo);
        tbUsers.setAccount(account);
        tbUsersDao.updateUserInfo(tbUsers);
        TbUserEvent event = new TbUserEvent();
        event.setEventDate(new Date());
        event.setAccount(account);
        event.setEvent(tbUsers.getName()+"用户更新用户信息");
        tbUserEventDao.insertEvent(event);
    }

    @Override
    public List<UserEventVo> getUserHistory(String account) {
        List<TbUserEvent> tbUserEvents = tbUserEventDao.selectEventByUser(account);
        List<UserEventVo> result = new ArrayList<>();
        if(tbUserEvents!=null&&tbUserEvents.size()>0) {
            for (TbUserEvent tbUserEvent : tbUserEvents) {
                result.add(getUserEventVo(tbUserEvent));
            }
        }
        return result;
    }

    private UserEventVo getUserEventVo(TbUserEvent tbUserEvent) {
        UserEventVo result = new UserEventVo();
        result.setEvent(tbUserEvent.getEvent());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = simpleDateFormat.format(tbUserEvent.getEventDate());
        result.setEventDate(format);
        return result;
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
        tbUsers.setMonthlySalary(tbUsersPo.getMonthlySalary());
        tbUsers.setJob(StringUtils.isEmpty(tbUsersPo.getJob()) ? "" : tbUsersPo.getJob());
        tbUsers.setFixedTel(StringUtils.isEmpty(tbUsersPo.getFixedTel()) ? "" : tbUsersPo.getFixedTel());
        tbUsers.setName(StringUtils.isEmpty(tbUsersPo.getName()) ? "" : tbUsersPo.getName());
        tbUsers.setIdentity(tbUsersPo.getIdentity()==null?0:tbUsersPo.getIdentity());
        tbUsers.setTurnPositiveDate(StringUtils.isEmpty(tbUsersPo.getTurnPositiveDate()) ? "" : tbUsersPo.getTurnPositiveDate());
        tbUsers.setNationality(StringUtils.isEmpty(tbUsersPo.getNationality()) ? "" : tbUsersPo.getNationality());
        tbUsers.setBranch(StringUtils.isEmpty(tbUsersPo.getBranch()) ? "" : tbUsersPo.getBranch());
        int basePay = 1;
        if (tbUsers.getIdentity()==1){
            basePay = 2;
        }
        tbUsers.setBasePay(basePay);
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
        String permissionValue = "";
        if (tbUsers.getPermission()==1) {
            permissionValue = "管理员";
        }else{
            permissionValue = "普通用户";
        }
        result.setPermissionValue(permissionValue);
        result.setEmail(StringUtils.isEmpty(tbUsers.getEmail()) ? "" : tbUsers.getEmail());
        result.setAccount(StringUtils.isEmpty(tbUsers.getAccount()) ? "" : tbUsers.getAccount());
        result.setName(StringUtils.isEmpty(tbUsers.getName()) ? "" : tbUsers.getName());
        result.setIdCard(StringUtils.isEmpty(tbUsers.getIdCard()) ? "" : tbUsers.getIdCard());
        result.setIdentity(tbUsers.getIdentity());
        String identityValue = "";
        if (tbUsers.getIdentity()==1){
            identityValue = "正是党员";
        }else{
            identityValue = "预备党员";
        }
        result.setIdentityValue(identityValue);
        result.setNationality(StringUtils.isEmpty(tbUsers.getNationality()) ? "" : tbUsers.getNationality());
        result.setBranch(StringUtils.isEmpty(tbUsers.getBranch()) ? "" : tbUsers.getBranch());
        result.setFixedTel(StringUtils.isEmpty(tbUsers.getFixedTel()) ? "" : tbUsers.getFixedTel());
        result.setAddress(StringUtils.isEmpty(tbUsers.getAddress()) ? "" : tbUsers.getAddress());
        result.setJob(StringUtils.isEmpty(tbUsers.getJob()) ? "" : tbUsers.getJob());
        result.setJoinPartyDate(StringUtils.isEmpty(tbUsers.getJoinPartyDate()) ? "" : tbUsers.getJoinPartyDate());
        result.setTurnPositiveDate(StringUtils.isEmpty(tbUsers.getTurnPositiveDate()) ? "" : tbUsers.getTurnPositiveDate());
        result.setMonthlySalary(tbUsers.getMonthlySalary());
        result.setUserImg(StringUtils.isEmpty(tbUsers.getUserImg())?"":tbUsers.getUserImg());
        return result;
    }
}
