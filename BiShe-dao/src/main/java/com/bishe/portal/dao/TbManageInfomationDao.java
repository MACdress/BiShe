package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbManageInformation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author gaopan31
 */
@Repository
public interface TbManageInfomationDao {

    /**
     * 新增一条咨讯
     * @param tbManageInformation 插入咨讯的入参
     */
    void insertInformationInfo(@Param("tbManageInformation") TbManageInformation tbManageInformation);
}
