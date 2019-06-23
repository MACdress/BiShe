package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbColumnInfoDao;
import com.bishe.portal.dao.TbColumnManageDao;
import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.model.mo.TbColumnInfo;
import com.bishe.portal.model.mo.TbColumnManage;
import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.ParamColumnInfoPo;
import com.bishe.portal.model.po.TbColumnManagePo;
import com.bishe.portal.model.vo.ColumnInfoSimpleVo;
import com.bishe.portal.model.vo.ColumnInfoVo;
import com.bishe.portal.model.vo.ShowColumnInfoVo;
import com.bishe.portal.service.ColumnManageService;
import com.bishe.portal.service.utils.QiniuWrapper;
import com.bishe.portal.service.utils.RedisCacheUtil;
import com.bishe.portal.service.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopan31
 */
@Service
public class ColumnManageServiceImpl implements ColumnManageService {
    @Resource
    private TbColumnManageDao tbColumnManageDao;

    @Resource
    private TbUsersDao tbUsersDao;

    @Resource
    private TbColumnInfoDao tbColumnInfoDao;
    @Resource
    private RedisCacheUtil redisCacheUtil;
    private static final Logger logger = LoggerFactory.getLogger(ColumnManageServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addColumnManage(ParamColumnInfoPo paramColumnInfoPo) {
        TbColumnManage tbColumnManage = getTbColumnManage(paramColumnInfoPo);
        tbColumnManageDao.insertColumnMange(tbColumnManage);
        logger.info("插入成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnManage(ParamColumnInfoPo paramColumnInfoPo) {
        TbColumnManage tbColumnManage = getTbColumnManage(paramColumnInfoPo);
        tbColumnManageDao.updateColumnMange(tbColumnManage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    public List<ShowColumnInfoVo> getAllColumnInfo() {
        List<ShowColumnInfoVo> result = new ArrayList<>();
        List<TbColumnManagePo> columnMangeList = tbColumnManageDao.getColumnInfoByListByParentId(0);
        if (columnMangeList!=null) {
            List<Integer> columnManageIdList = new ArrayList<>();
            for (TbColumnManagePo tbColumnManagePo : columnMangeList) {
                columnManageIdList.add(tbColumnManagePo.getId());
            }
            if(columnManageIdList.size()<=0){
                logger.info("栏目返回值为空");
                return result;
            }
            List<TbColumnInfo> columnInfoList = tbColumnInfoDao.getColumnListByColumnId(columnManageIdList);
            Map<Integer, List<TbColumnInfo>> columnInfoMap = getColumnInfoMap(columnInfoList);
            for(TbColumnManagePo columnManagePo:columnMangeList){
                ShowColumnInfoVo showColumnInfoVo = new ShowColumnInfoVo();
                showColumnInfoVo.setColumnManageId(columnManagePo.getId());
                showColumnInfoVo.setColumnManageName(columnManagePo.getColumnName());
                List<TbColumnInfo> tbColumnInfos = columnInfoMap.get(columnManagePo.getId());
                if ((tbColumnInfos !=null)&&tbColumnInfos.size()>0) {
                    List<ColumnInfoSimpleVo> columnInfoSimpleVos = new ArrayList<>();
                    for (TbColumnInfo tbColumnInfo : tbColumnInfos) {
                        ColumnInfoSimpleVo columnInfoSimpleVo = getColumnInfoSimpleVo(tbColumnInfo);
                        columnInfoSimpleVos.add(columnInfoSimpleVo);
                    }
                    showColumnInfoVo.setColumnInfoList(columnInfoSimpleVos);
                    result.add(showColumnInfoVo);
                }
            }
        }
        return result;
    }

    private ColumnInfoSimpleVo getColumnInfoSimpleVo(TbColumnInfo tbColumnInfo) {
        ColumnInfoSimpleVo result = new ColumnInfoSimpleVo();
        result.setBelongColumn(tbColumnInfo.getBelongColumn());
        result.setIsTop(tbColumnInfo.getIsTop());
        result.setSummary(tbColumnInfo.getSummary());
        result.setId(tbColumnInfo.getId());
        return result;
    }

    private Map<Integer,List<TbColumnInfo>> getColumnInfoMap(List<TbColumnInfo> columnInfoList) {
        Map<Integer,List<TbColumnInfo>> result = new HashMap<>();
        for (TbColumnInfo tbColumnInfo:columnInfoList){
            Integer belongColumnId = tbColumnInfo.getBelongColumn();
            List<TbColumnInfo> tbColumnInfoList = new ArrayList<>();
            if(result.containsKey(belongColumnId)){
                tbColumnInfoList = result.get(belongColumnId);
            }
            tbColumnInfoList.add(tbColumnInfo);
            result.put(belongColumnId,tbColumnInfoList);
        }
        return result;
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
            List<TbColumnInfo> tbColumnInfos = tbColumnInfoDao.getColumnInfoByBelongColumn(tbColumnManagePo.getId());
            columnInfoVo.setTotal(tbColumnInfos.size());
            columnInfoVo.setId(tbColumnManagePo.getId());
            return columnInfoVo;
     }

    private TbColumnManage getTbColumnManage(ParamColumnInfoPo paramColumnInfoPo) {
        TbColumnManage tbColumnManage = new TbColumnManage();
        tbColumnManage.setParent(paramColumnInfoPo.getParentId()==null?0:paramColumnInfoPo.getParentId());
        tbColumnManage.setColumnImg(paramColumnInfoPo.getColumnImg());
        tbColumnManage.setColumnName(paramColumnInfoPo.getColumnName());
        tbColumnManage.setColumnStatus(paramColumnInfoPo.getColumnStatus()==null?0:paramColumnInfoPo.getColumnStatus());
        tbColumnManage.setIsReview(paramColumnInfoPo.getIsReview()==null?0:paramColumnInfoPo.getIsReview());
        tbColumnManage.setReviewUser(paramColumnInfoPo.getReviewUserId());
        tbColumnManage.setColumnText(paramColumnInfoPo.getColumnText());
        tbColumnManage.setCreateUser(paramColumnInfoPo.getCreateUserId());
        tbColumnManage.setId(paramColumnInfoPo.getId()==null?0:paramColumnInfoPo.getId());
        return tbColumnManage;
    }
}
