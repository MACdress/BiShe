package com.bishe.portal.service;

import com.bishe.portal.model.vo.FindColumnInfoParamVo;
import com.bishe.portal.model.vo.ManageColumnInfoParamVo;
import com.bishe.portal.model.vo.ManageColumnInfoSimpleVo;
import com.bishe.portal.model.vo.ManageColumnInfoVo;

import java.util.List;

/**
 * @author gaopan31
 */
public interface TbColumnInfoService {
    /**新增一条咨讯
     * @param manageInformationParamVo 增加一条咨讯的入参
     */
    void addInformationInfo(ManageColumnInfoParamVo manageInformationParamVo);

    void setIsTop(Integer id);

    void deleteColumnInfo(Integer id);

    void updateColumnInfo(ManageColumnInfoParamVo manageInformationParamVo);

    List<ManageColumnInfoSimpleVo> getColumnInfoList(FindColumnInfoParamVo findColumnInfoParamVo);

    ManageColumnInfoVo getColumnInfoDetail(Integer id);
}
