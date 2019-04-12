package com.bishe.portal.service;

import com.bishe.portal.model.vo.ManageInformationParamVo;

/**
 * @author gaopan31
 */
public interface InformationManageService {
    /**新增一条咨讯
     * @param manageInformationParamVo 增加一条咨讯的入参
     */
    void addInformationInfo(ManageInformationParamVo manageInformationParamVo,int userId);
}
