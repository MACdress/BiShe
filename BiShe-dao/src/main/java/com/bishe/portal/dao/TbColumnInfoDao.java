package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbColumnInfo;
import com.bishe.portal.model.vo.FindColumnInfoParamVo;
import com.bishe.portal.model.vo.ManageColumnInfoParamVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gaopan31
 */
@Repository
public interface TbColumnInfoDao {

    /**
     * 新增一条咨讯
     * @param tbManageInformation 插入咨讯的入参
     */
    void insertInformationInfo(@Param("tbColumnInfo") TbColumnInfo tbManageInformation);

    void setIsTop(@Param("id") Integer id);



    void deleteColumnInfo(@Param("id") Integer id);

    void updateColumnInfo(@Param("columnInfo") ManageColumnInfoParamVo manageInformationParamVo);

    TbColumnInfo getColumnInfoById(@Param("id") Integer id);

    List<TbColumnInfo> selectColumnInfoList(@Param("columnInfoParam") FindColumnInfoParamVo findColumnInfoParamVo);

    void setAllIsUnTop(@Param("belongColumn") Integer belongColumn);
}
