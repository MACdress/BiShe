package com.bishe.portal.service;

import com.bishe.portal.model.po.ParamColumnInfoPo;
import com.bishe.portal.model.vo.ColumnInfoVo;

import java.util.List;

/**
 * @author gaopan31
 */
public interface ColumnManageService{

    /**
     * 新增一条栏目信息
     * @param paramColumnInfoPo 新增栏目的入参
     */
    void addColumnManage(ParamColumnInfoPo paramColumnInfoPo);

    /**
     *更新一条栏目信息
     * @param paramColumnInfoPo 更新栏目的入参
     */
    void updateColumnManage(ParamColumnInfoPo paramColumnInfoPo);

    /**
     * 删除一条栏目
     * @param id 栏目ID
     */
    void deleteColumnInfo(int id);

    /**
     * 生成一个栏目ID并返回
     * @return 返回栏目ID
     */
    String getColumnId();

    /**
     * 根据栏目名称获取栏目信息
     * @param columnName 栏目名称
     * @return 符合条件的栏目信息
     */
    List<ColumnInfoVo> getColumnInfoByName(String columnName);

    /**
     * 根据父栏目Id获取栏目集合
     * @param columnId 栏目ID
     * @return 返回符合条件的栏目信息
     */
    List<ColumnInfoVo> getAllParentColumnList(String columnId);

    List<ColumnInfoVo> getAllParentColumn();
}
