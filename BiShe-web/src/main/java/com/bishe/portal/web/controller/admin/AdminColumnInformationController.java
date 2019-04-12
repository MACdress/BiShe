package com.bishe.portal.web.controller.admin;

import com.bishe.portal.model.vo.ManageInformationParamVo;
import com.bishe.portal.service.InformationManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author gaopan31
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/admin/InformationManage")
public class AdminColumnInformationController {

    @Resource
    InformationManageService informationManageService;

    /**
     * 新增一个咨讯信息
     * @return 返回响应
     */
    public String addInformationInfo (@RequestBody ManageInformationParamVo manageInformationParamVo){

        informationManageService.addInformationInfo(manageInformationParamVo);

    }
}
