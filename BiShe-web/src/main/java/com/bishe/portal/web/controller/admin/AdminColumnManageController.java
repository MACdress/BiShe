package com.bishe.portal.web.controller.admin;

import com.bishe.portal.model.po.ParamColumnInfoPo;
import com.bishe.portal.model.vo.ColumnInfoVo;
import com.bishe.portal.model.vo.UserInfoVo;
import com.bishe.portal.service.ColumnManageService;
import com.bishe.portal.web.utils.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author gaopan31
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/admin/columnManage")
public class AdminColumnManageController {
    @Resource
    ColumnManageService columnManageService;

    /**增加一条栏目，如果是父级，则
     * @param paramColumnInfoPo 接收新增栏目的参数
     * @return 返回响应
     */
    @RequestMapping(value = "addColumnInfo",method = RequestMethod.POST)
    @ResponseBody
    public String addColumn(@RequestBody  ParamColumnInfoPo paramColumnInfoPo ,HttpSession httpSession){
        UserInfoVo userInfoVo = (UserInfoVo)httpSession.getAttribute("user");
        if ((userInfoVo == null)||(userInfoVo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        paramColumnInfoPo.setCreateUserId(userInfoVo.getAccount());
        columnManageService.addColumnManage(paramColumnInfoPo);
        return JsonView.render(200,"insert success");
    }


//    /**
//     * 新增时的页面信息
//     * @return 返回响应
//     */
//    @RequestMapping(value = "showAddColumn",method = RequestMethod.GET)
//    @ResponseBody
//    public String addColumnPage(HttpSession httpSession){
//        UserInfoVo userInfoVo = (UserInfoVo)httpSession.getAttribute("user");
//        if ((userInfoVo == null)||(userInfoVo.getPermission()!=1)){
//            return JsonView.render(404,"user is not admin");
//        }
//        String columnId = columnManageService.getColumnId();
//        JsonObject result = new JsonObject();
//        result.addProperty("column_id",columnId);
//        return JsonView.render(200,"success",result);
//    }

    /**
     * 更新栏目信息
     * @param paramColumnInfoPo 接收更新栏目的参数
     * @return 返回响应
     */
    @RequestMapping(value = "modify",method = RequestMethod.POST)
    @ResponseBody
    public String modifyColumn(@RequestBody ParamColumnInfoPo paramColumnInfoPo,HttpSession httpSession){
        UserInfoVo userInfoVo = (UserInfoVo)httpSession.getAttribute("user");
        if ((userInfoVo == null)||(userInfoVo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        paramColumnInfoPo.setCreateUserId(userInfoVo.getAccount());
        columnManageService.updateColumnManage(paramColumnInfoPo);
        return JsonView.render(200,"update success");
    }

    /**
     * 删除栏目信息
     * @param id 栏目ID
     * @return 返回响应
     */
    @RequestMapping(value = "deleteColumn",method = RequestMethod.GET)
    @ResponseBody
    public String deleteColumn(Integer id,HttpSession httpSession){
        UserInfoVo userInfoVo = (UserInfoVo)httpSession.getAttribute("user");
        if ((userInfoVo == null)||(userInfoVo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        if (id == null){
            return JsonView.render(301,"入参错误");
        }
        int columnId = id == null?0:id.intValue();
        columnManageService.deleteColumnInfo(columnId);
        return JsonView.render(200,"delete success");
    }

    /**
     * 通过栏目名称获取信息
     * @param columnName 栏目名称
     * @return 返回响应
     */
    @RequestMapping(value = "getParentInfoByName",method = RequestMethod.GET)
    @ResponseBody
    public String getParentNameByColumnName(String columnName,HttpSession httpSession){
        UserInfoVo userInfoVo = (UserInfoVo)httpSession.getAttribute("user");
        if ((userInfoVo == null)||(userInfoVo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        List<ColumnInfoVo> rs = columnManageService.getColumnInfoByName(columnName);
        return  JsonView.render(200,"success",rs);
    }

    /**
     * 通过父级栏目ID获取栏目集合
     * @param  columnId 父级栏目ID
     * @return 返回响应
     */
    @RequestMapping(value = "getColumnByParent", method = RequestMethod.GET)
    @ResponseBody
    public String getAllParentColumn(String columnId,HttpSession httpSession){
        UserInfoVo userInfoVo = (UserInfoVo)httpSession.getAttribute("user");
        if ((userInfoVo == null)||(userInfoVo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        List<ColumnInfoVo> rs = columnManageService.getAllParentColumnList(columnId);
        return  JsonView.render(200,"success",rs);
    }

    /**
     * 获取所有的父级栏目
     * @return 返回响应
     */
    @RequestMapping(value = "getAllParentColumn", method = RequestMethod.GET)
    @ResponseBody
    public String getAllParentColumn(HttpSession httpSession){
        UserInfoVo userInfoVo = (UserInfoVo)httpSession.getAttribute("user");
        if ((userInfoVo == null)||(userInfoVo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        List<ColumnInfoVo> result  = columnManageService.getAllParentColumn();
        return  JsonView.render(200,"success",result);
    }
}
