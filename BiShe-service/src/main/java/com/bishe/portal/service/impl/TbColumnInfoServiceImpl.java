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
import com.bishe.portal.service.utils.COSClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final Logger logger = LoggerFactory.getLogger(TbColumnInfoServiceImpl.class);


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addInformationInfo(ManageColumnInfoParamVo manageInformationParamVo) {
        try {
            String imgUrl = "";
            if (manageInformationParamVo.getColumnImg() != null) {
                COSClientUtil cosClientUtil = new COSClientUtil();
                String name = cosClientUtil.uploadFile2Cos(manageInformationParamVo.getColumnImg());
                imgUrl = cosClientUtil.getImgUrl(name);
            }
            TbColumnInfo tbManageInformation = getTbManageInformation(manageInformationParamVo);
            tbManageInformation.setInfoImg(imgUrl);
            tbColumnInfoDao.insertInformationInfo(tbManageInformation);
        }catch (Exception e){
            logger.info("添加栏目信息异常");
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setIsTop(Integer id) {
        TbColumnInfo columnInfo = tbColumnInfoDao.getColumnInfoById(id);
        tbColumnInfoDao.setAllIsUnTop(columnInfo.getBelongColumn());
        tbColumnInfoDao.setIsTop(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteColumnInfo(Integer id) {
        tbColumnInfoDao.deleteColumnInfo(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnInfo(ManageColumnInfoParamVo manageInformationParamVo) {
        try {
            String imgUrl = "";
            if (manageInformationParamVo.getColumnImg() != null) {
                COSClientUtil cosClientUtil = new COSClientUtil();
                String name = cosClientUtil.uploadFile2Cos(manageInformationParamVo.getColumnImg());
                imgUrl = cosClientUtil.getImgUrl(name);
            }
            TbColumnInfo tbManageInformation = getTbManageInformation(manageInformationParamVo);
            tbManageInformation.setInfoImg(imgUrl);
            tbColumnInfoDao.updateColumnInfo(tbManageInformation);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        TbColumnManage columnManage = tbColumnManageDao.getColumnInfoById(tbColumnInfo.getBelongColumn());
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
        Integer belongColumn = tbColumnInfo.getBelongColumn();
        int belongColumnId = 0;
        if (belongColumn != null){
            belongColumnId = tbColumnInfo.getBelongColumn();
        }
        TbColumnManage columnManage = tbColumnManageDao.getColumnInfoById(belongColumnId);
        result.setBelongColumnName(columnManage==null?"":columnManage.getColumnName());
        result.setInformationStatus(tbColumnInfo.getInformationStatus());
        result.setTittle(tbColumnInfo.getTittle());
        result.setCreateUser(tbColumnInfo.getCreateUser());
        TbUsers userInfo = tbUsersDao.getUserInfoByAccount(tbColumnInfo.getCreateUser());
        result.setCreateUserName(userInfo == null ?"":userInfo.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        result.setCreateAt(sdf.format(tbColumnInfo.getCreateAt()));
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
        tbColumnInfo.setCreateUser(manageInformationParamVo.getCreateUser());
        tbColumnInfo.setIsTop(0);
        return tbColumnInfo;
    }
}
