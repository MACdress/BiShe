package com.bishe.portal.web.controller;

import com.bishe.portal.model.vo.ManageColumnInfoVo;
import com.bishe.portal.model.vo.ShowColumnInfoVo;
import com.bishe.portal.service.ColumnManageService;
import com.bishe.portal.service.TbColumnInfoService;
import com.bishe.portal.web.utils.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/columnManage")
public class ColumnManageController {
    @Resource
    ColumnManageService columnManageService;
    @Resource
    TbColumnInfoService tbColumnInfoService;

    @RequestMapping(value = "getAllColumnInfo",method = RequestMethod.GET)
    @ResponseBody
    public String getAllColumnInfo(){
        List<ShowColumnInfoVo> columnInfoVoList = columnManageService.getAllColumnInfo();
        return JsonView.render(200,"获取成功",columnInfoVoList);
    }
    @RequestMapping(value = "getColumnDetailInfo",method = RequestMethod.GET)
    @ResponseBody
    public String getColumnDetailInfo(@RequestParam("id")Integer id){
        if ((id == null)||(id==0)){
            return JsonView.render(404,"入参不完整");
        }
        ManageColumnInfoVo manageColumnInfoParamVo = tbColumnInfoService.getColumnInfoDetail(id);
        return JsonView.render(200,"查询成功",manageColumnInfoParamVo);
    }
}
