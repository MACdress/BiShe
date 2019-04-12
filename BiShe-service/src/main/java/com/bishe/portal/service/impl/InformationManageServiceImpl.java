package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbManageInfomationDao;
import com.bishe.portal.model.mo.TbManageInformation;
import com.bishe.portal.model.vo.ManageInformationParamVo;
import com.bishe.portal.service.InformationManageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gaopan31
 */
@Service
public class InformationManageServiceImpl implements InformationManageService {

    @Resource
    TbManageInfomationDao tbManageInfomationDao;

    @Override
    public void addInformationInfo(ManageInformationParamVo manageInformationParamVo,int userId) {
        TbManageInformation tbManageInformation = getTbManageInformation(manageInformationParamVo,userId);
        tbManageInfomationDao.insertInformationInfo(tbManageInformation);
    }

    private TbManageInformation getTbManageInformation(ManageInformationParamVo manageInformationParamVo,int userId) {
        TbManageInformation tbManageInformation = new TbManageInformation();
        tbManageInformation.setAuthor(manageInformationParamVo.getAuthor());
        tbManageInformation.setBelongColumn(manageInformationParamVo.getBelongColumn());
        tbManageInformation.setCommentBeginTime(tbManageInformation.getCommentBeginTime());
        tbManageInformation.setCommentEndTime(manageInformationParamVo.getCommentEndTime());
        tbManageInformation.setDownTime(manageInformationParamVo.getDownTime());
        tbManageInformation.setCreateUser(userId);
        return tbManageInformation;
    }
}
