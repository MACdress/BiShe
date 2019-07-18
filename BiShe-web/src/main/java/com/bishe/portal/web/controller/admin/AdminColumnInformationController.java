package com.bishe.portal.web.controller.admin;

import com.bishe.portal.model.vo.*;
import com.bishe.portal.service.TbColumnInfoService;
import com.bishe.portal.web.utils.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author gaopan31
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/admin/InformationManage")
public class AdminColumnInformationController {

    @Resource
    TbColumnInfoService tbColumnInfoService;

    /**
     * 新增一个咨讯信息
     * @return 返回响应
     */
    @RequestMapping(value = "addInformation", method = RequestMethod.POST)
    @ResponseBody
    public String addInformationInfo (@RequestBody ManageColumnInfoParamVo manageInformationParamVo, HttpSession httpSession){
        if (manageInformationParamVo == null){
            return JsonView.render(404,"新增失败");
        }
        UserInfoVo userInfoVo = (UserInfoVo)httpSession.getAttribute("user");
        if ((userInfoVo == null)||(userInfoVo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        manageInformationParamVo.setCreateUser(userInfoVo.getAccount());
        tbColumnInfoService.addInformationInfo(manageInformationParamVo);
        return JsonView.render(200,"新增成功");
    }

    /**
     * 查询某个栏目下的资讯
     * @param findColumnInfoParamVo 查询的入参
     * @return 返回响应
     */
    @RequestMapping(value = "getColumnInfo",method = RequestMethod.POST)
    @ResponseBody
    public String getColumnInformation(@RequestBody FindColumnInfoParamVo findColumnInfoParamVo){
        if ((findColumnInfoParamVo == null)||(findColumnInfoParamVo.getBelongColumn() == null)){
            return JsonView.render(404,"入参有误");
        }
        List<ManageColumnInfoSimpleVo> result =  tbColumnInfoService.getColumnInfoList(findColumnInfoParamVo);
        return JsonView.render(200,"查询成功",result);
    }

    /**
     * 查找某个资讯的详细信息
     * @param id  资讯的id
     * @return 返回响应
     */
    @RequestMapping(value = "getColumnInfoDetail",method = RequestMethod.GET)
    @ResponseBody
    public String getColumnInformationDetail(Integer id){
        if ((id == null)||(id == 0)){
            return JsonView.render(404,"入参有误");
        }
        ManageColumnInfoVo manageColumnInfoParamVo = tbColumnInfoService.getColumnInfoDetail(id);
        return JsonView.render(200,"查询成功",manageColumnInfoParamVo);
    }

    /**
     * 置顶资讯
     * @param manageColumnInfoParamVo 资讯id
     * @return 返回响应
     */
    @RequestMapping(value = "setTop",method = RequestMethod.POST)
    @ResponseBody
    public String setIsTop(@RequestBody ManageColumnInfoParamVo manageColumnInfoParamVo){
        if ((manageColumnInfoParamVo.getId() == null)||(manageColumnInfoParamVo.getId() == 0)){
            return JsonView.render(404,"入参有误");
        }
        tbColumnInfoService.setIsTop(manageColumnInfoParamVo.getId());
        return JsonView.render(200,"更新成功");
    }

    /**
     * 删除资讯
     * @param id 资讯id
     * @return 返回响应
     */
    @RequestMapping(value = "deleteColumnInfo",method = RequestMethod.GET)
    @ResponseBody
    public String deleteColumnInfo(Integer id){
        if ((id == null)||(id == 0)){
            return JsonView.render(404,"入参有误");
        }
        tbColumnInfoService.deleteColumnInfo(id);
        return JsonView.render(200,"删除成功");
    }

    /**
     * 更新资讯
     * @param manageInformationParamVo 资讯信息
     * @return 返回响应
     */
    @RequestMapping(value = "updateColumnInfo",method = RequestMethod.POST)
    @ResponseBody
    public String updateColumnInfo (@RequestBody ManageColumnInfoParamVo manageInformationParamVo){
        if (manageInformationParamVo == null){
            return JsonView.render(404,"更新失败");
        }
        tbColumnInfoService.updateColumnInfo(manageInformationParamVo);
        return JsonView.render(200,"更新成功");
    }
}
