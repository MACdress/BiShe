package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbColumnManage;
import com.bishe.portal.model.po.TbColumnManagePo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gaopan31
 */
@Repository
public interface TbColumnManageDao {

    /**
     * 插入栏目信息
     * @param tbColumnManage 插入发出的栏目信息
     */
    void insertColumnMange(@Param("tbColumnManage") TbColumnManage tbColumnManage);

    /**
     * 更新栏目信息
     * @param tbColumnManage 更新的栏目信息
     */
    void updateColumnMange(@Param("tbColumnManage") TbColumnManage tbColumnManage);

    /**
     * 通过栏目ID获取栏目信息
     * @param id 栏目ID
     * @return 栏目信息
     */
    TbColumnManage getColumnInfoById (@Param("id")int id);

    /**
     * 删除一个栏目
     * @param id  栏目ID
     */
    void deleteColumnInfo(@Param("id")int id);

    /**
     * 获取栏目列表(可以用过栏目名称)
     * @param columnName 栏目名称
     * @return 栏目集合
     */
    List<TbColumnManagePo> getColumnInfoByList(@Param("columnName") String columnName);

    /**
     * 通过parent获取栏目集合
     * @param columnId 父栏目ID
     * @return 栏目集合
     */
    List<TbColumnManagePo> getColumnInfoByListByParentId(@Param("parent") int columnId);
}
