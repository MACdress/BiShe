package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbColumnInfoDao;
import com.bishe.portal.dao.TbColumnManageDao;
import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.model.mo.TbColumnInfo;
import com.bishe.portal.model.mo.TbColumnManage;
import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.vo.FindColumnInfoParamVo;
import com.bishe.portal.model.vo.ManageColumnInfoParamVo;
import com.bishe.portal.model.vo.ManageColumnInfoSimpleVo;
import com.bishe.portal.model.vo.ManageColumnInfoVo;
import com.bishe.portal.service.TbColumnInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gaopan31
 */
@Service
public class TbColumnInfoServiceImpl implements TbColumnInfoService {

    @Resource
    TbColumnInfoDao tbColumnInfoDao;
    @Resource
    TbUsersDao tbUsersDao;
    @Resource
    TbColumnManageDao tbColumnManageDao;

    @Override
    public void addInformationInfo(ManageColumnInfoParamVo manageInformationParamVo) {
        TbColumnInfo tbManageInformation = getTbManageInformation(manageInformationParamVo);
        tbColumnInfoDao.insertInformationInfo(tbManageInformation);
    }

    @Override
    public void setIsTop(Integer id) {
        TbColumnInfo columnInfo = tbColumnInfoDao.getColumnInfoById(id);
        tbColumnInfoDao.setAllIsUnTop(columnInfo.getBelongColumn());
        tbColumnInfoDao.setIsTop(id);
    }

    @Override
    public void deleteColumnInfo(Integer id) {
        tbColumnInfoDao.deleteColumnInfo(id);
    }

    @Override
    public void updateColumnInfo(ManageColumnInfoParamVo manageInformationParamVo) {
        tbColumnInfoDao.updateColumnInfo(manageInformationParamVo);
    }

    @Override
    public List<ManageColumnInfoSimpleVo> getColumnInfoList(FindColumnInfoParamVo findColumnInfoParamVo) {
        if (findColumnInfoParamVo.getCreateBeginTime()!=null){
            if (findColumnInfoParamVo.getCreateEndTime()==null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String endTime = dateFormat.format(date);
                findColumnInfoParamVo.setCreateEndTime(endTime);
            }
        }
        List<TbColumnInfo> columnInfoList = tbColumnInfoDao.selectColumnInfoList(findColumnInfoParamVo);
        List<ManageColumnInfoSimpleVo> result = new ArrayList<>();
        for (TbColumnInfo tbColumnInfo:columnInfoList){
            result.add(getManageColumnInfoSimple(tbColumnInfo));
        }
        return result;
    }

    @Override
    public ManageColumnInfoVo getColumnInfoDetail(Integer id) {
        TbColumnInfo tbColumnInfo= tbColumnInfoDao.getColumnInfoById(id);
        return getManageColumnInfo(tbColumnInfo);
    }


    private  ManageColumnInfoVo getManageColumnInfo(TbColumnInfo tbColumnInfo){
        ManageColumnInfoVo result = new ManageColumnInfoVo();
        result.setAuthor(tbColumnInfo.getAuthor());
        result.setBelongColumn(tbColumnInfo.getBelongColumn());
        TbColumnManage columnManage = tbColumnManageDao.getColumnInfoById(tbColumnInfo.getBelongColumn().toString());
        result.setBelongColumnName(columnManage.getColumnName());
        result.setCreateUser(tbColumnInfo.getCreateUser());
        TbUsers userInfo = tbUsersDao.getUserInfoByAccount(tbColumnInfo.getCreateUser());
        result.setCreateUserName(userInfo == null ?"":userInfo.getName());
        result.setDownTime(tbColumnInfo.getDownTime());
        result.setInformationStatus(tbColumnInfo.getInformationStatus());
        result.setIsComment(tbColumnInfo.getIsComment());
        result.setId(tbColumnInfo.getId());
        result.setIsTop(tbColumnInfo.getIsTop());
        result.setOrigin(tbColumnInfo.getOrigin());
        result.setOriginUrl(tbColumnInfo.getOriginUrl());
        result.setSummary(tbColumnInfo.getSummary());
        result.setText(tbColumnInfo.getText());
        result.setTittle(tbColumnInfo.getTittle());
        /*
          这里不知道是否需要改变
         */
        result.setCreateAt(tbColumnInfo.getCreateAt().toString());
        return  result;
    }

    private ManageColumnInfoSimpleVo getManageColumnInfoSimple(TbColumnInfo tbColumnInfo){
        ManageColumnInfoSimpleVo result = new ManageColumnInfoSimpleVo();
        result.setId(tbColumnInfo.getId());
        result.setBelongColumn(tbColumnInfo.getBelongColumn());
        TbColumnManage columnManage = tbColumnManageDao.getColumnInfoById(tbColumnInfo.getBelongColumn().toString());
        result.setBelongColumnName(columnManage.getColumnName());
        result.setInformationStatus(tbColumnInfo.getInformationStatus());
        result.setTittle(tbColumnInfo.getTittle());
        result.setCreateUser(tbColumnInfo.getCreateUser());
        TbUsers userInfo = tbUsersDao.getUserInfoByAccount(tbColumnInfo.getCreateUser());
        result.setCreateUserName(userInfo == null ?"":userInfo.getName());
        result.setCreateAt(tbColumnInfo.getCreateAt().toString());
        return result;
    }

    private TbColumnInfo getTbManageInformation(ManageColumnInfoParamVo manageInformationParamVo) {
        TbColumnInfo tbColumnInfo = new TbColumnInfo();
        tbColumnInfo.setAuthor(manageInformationParamVo.getAuthor());
        tbColumnInfo.setBelongColumn(manageInformationParamVo.getBelongColumn());
        tbColumnInfo.setCommentBeginTime(manageInformationParamVo.getCommentBeginTime());
        tbColumnInfo.setCommentEndTime(manageInformationParamVo.getCommentEndTime());
        tbColumnInfo.setDownTime(manageInformationParamVo.getDownTime());
        tbColumnInfo.setOrigin(manageInformationParamVo.getOrigin());
        tbColumnInfo.setInformationStatus(manageInformationParamVo.getInformationStatus());
        tbColumnInfo.setIsComment(manageInformationParamVo.getIsComment());
        tbColumnInfo.setOriginUrl(manageInformationParamVo.getOriginUrl());
        tbColumnInfo.setSummary(manageInformationParamVo.getSummary());
        tbColumnInfo.setText(manageInformationParamVo.getText());
        tbColumnInfo.setTittle(manageInformationParamVo.getTittle());
        return tbColumnInfo;
    }
}
