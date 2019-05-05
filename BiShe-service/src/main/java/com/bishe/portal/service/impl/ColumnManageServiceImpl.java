package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbColumnManageDao;
import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.model.mo.TbColumnManage;
import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.ParamColumnInfoPo;
import com.bishe.portal.model.po.TbColumnManagePo;
import com.bishe.portal.model.vo.ColumnInfoVo;
import com.bishe.portal.service.ColumnManageService;
import com.bishe.portal.service.utils.UUIDUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopan31
 */
@Service
public class ColumnManageServiceImpl implements ColumnManageService {
    @Resource
    private TbColumnManageDao tbColumnManageDao;

    @Resource
    private TbUsersDao tbUsersDao;

    @Override
    public void addColumnManage(ParamColumnInfoPo paramColumnInfoPo) {
        TbColumnManage tbColumnManage = getTbColumnManage(paramColumnInfoPo);
        tbColumnManageDao.insertColumnMange(tbColumnManage);
    }

    @Override
    public void updateColumnManage(ParamColumnInfoPo paramColumnInfoPo) {
        TbColumnManage tbColumnManage = getTbColumnManage(paramColumnInfoPo);
        tbColumnManageDao.updateColumnMange(tbColumnManage);
    }

    @Override
    public void deleteColumnInfo(int id) {
        tbColumnManageDao.deleteColumnInfo(id);
    }

    @Override
    public String getColumnId() {
        return UUIDUtils.getUUID(8);
    }

    @Override
    public List<ColumnInfoVo> getColumnInfoByName(String columnName) {
        List<TbColumnManagePo> tbColumnManages = tbColumnManageDao.getColumnInfoByList(columnName);
        return getColumnInfoList(tbColumnManages);
    }

    @Override
    public List<ColumnInfoVo> getAllParentColumnList(String columnId) {
        List<TbColumnManagePo> tbColumnManages = tbColumnManageDao.getColumnInfoByListByParentId(Integer.valueOf(columnId));
        return getColumnInfoList(tbColumnManages);
    }

    @Override
    public List<ColumnInfoVo> getAllParentColumn() {
        List<TbColumnManagePo> tbColumnManagePos = tbColumnManageDao.getColumnInfoByListByParentId(0);
        return getColumnInfoList(tbColumnManagePos);
    }

    private  List<ColumnInfoVo> getColumnInfoList(List<TbColumnManagePo> tbColumnManages){
        List<ColumnInfoVo> result = new ArrayList<>();
        for (TbColumnManagePo tbColumnManagePo : tbColumnManages){
            ColumnInfoVo columnInfoVo = getColumnInfoVo(tbColumnManagePo);
            result.add(columnInfoVo);
        }
        return result;
    }

    private ColumnInfoVo getColumnInfoVo (TbColumnManagePo tbColumnManagePo){
            ColumnInfoVo columnInfoVo = new ColumnInfoVo();
            columnInfoVo.setCreateUserId(tbColumnManagePo.getCreateUserId());
            TbUsers userInfoByAccount1 = tbUsersDao.getUserInfoByAccount(tbColumnManagePo.getCreateUserId());
            columnInfoVo.setCreateUserName(userInfoByAccount1 == null ?"":tbUsersDao.getUserInfoByAccount(tbColumnManagePo.getCreateUserId()).getName());
            columnInfoVo.setColumnName(tbColumnManagePo.getColumnName());
            columnInfoVo.setParentId(tbColumnManagePo.getParentId());
            TbColumnManage tbColumnManage = tbColumnManageDao.getColumnInfoById(tbColumnManagePo.getParentId());
            columnInfoVo.setParentName(tbColumnManage== null?"":tbColumnManage.getColumnName());
            columnInfoVo.setColumnImg(tbColumnManagePo.getColumnImg());
            columnInfoVo.setColumnStatus(tbColumnManagePo.getColumnStatus());
            columnInfoVo.setIsReview(tbColumnManagePo.getIsReview());
            columnInfoVo.setReviewUserId(tbColumnManagePo.getReviewUserId());
            TbUsers userInfoByAccount = tbUsersDao.getUserInfoByAccount(tbColumnManagePo.getReviewUserId());
            columnInfoVo.setReviewUserName(userInfoByAccount == null ? "" : userInfoByAccount.getName());
            columnInfoVo.setColumnText(tbColumnManagePo.getColumnText());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            columnInfoVo.setCreateTime(simpleDateFormat.format(tbColumnManagePo.getCreateAt()).trim());
            /*
                 新闻总数待开发
             */
            columnInfoVo.setTotal(0);
            return columnInfoVo;
     }

    private TbColumnManage getTbColumnManage(ParamColumnInfoPo paramColumnInfoPo) {
        TbColumnManage tbColumnManage = new TbColumnManage();
        tbColumnManage.setParent(paramColumnInfoPo.getParentId());
        tbColumnManage.setColumnImg(paramColumnInfoPo.getColumnImg());
        tbColumnManage.setColumnName(paramColumnInfoPo.getColumnName());
        tbColumnManage.setColumnStatus(paramColumnInfoPo.getColumnStatus());
        tbColumnManage.setIsReview(paramColumnInfoPo.getIsReview());
        tbColumnManage.setReviewUser(paramColumnInfoPo.getReviewUserId());
        tbColumnManage.setColumnText(paramColumnInfoPo.getColumnText());
        tbColumnManage.setCreateUser(paramColumnInfoPo.getCreateUserId());
        return tbColumnManage;
    }
}
