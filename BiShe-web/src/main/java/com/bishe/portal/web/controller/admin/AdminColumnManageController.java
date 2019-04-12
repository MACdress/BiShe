package com.bishe.portal.web.controller.admin;

import com.bishe.portal.model.po.ParamColumnInfoPo;
import com.bishe.portal.model.vo.ColumnInfoVo;
import com.bishe.portal.service.ColumnManageService;
import com.bishe.portal.web.utils.JsonView;
import com.google.gson.JsonObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gaopan31
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/admin/ColumnManage")
public class AdminColumnManageController {
    @Resource
    ColumnManageService columnManageService;

    /**增加一条栏目，如果是父级，则
     * @param paramColumnInfoPo 接收新增栏目的参数
     * @return 返回响应
     */
    @RequestMapping(value = "/add_column_info",method = RequestMethod.POST)
    public String addColumn(@RequestBody  ParamColumnInfoPo paramColumnInfoPo){
        paramColumnInfoPo.setCreateUserId((Integer) SecurityUtils.getSubject().getSession().getAttribute("userInfoId"));
        columnManageService.addColumnManage(paramColumnInfoPo);
        return JsonView.render(200,"插入成功");
    }


    /**
     * 新增时的页面信息
     * @return 返回响应
     */
    @RequestMapping("/show_add_column")
    public String addColumnPage(){
        String columnId = columnManageService.getColumnId();
        JsonObject result = new JsonObject();
        result.addProperty("column_id",columnId);
        return JsonView.render(200,"成功",result);
    }

    /**
     * 更新栏目信息
     * @param paramColumnInfoPo 接收更新栏目的参数
     * @return 返回响应
     */
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public String modifyColumn(ParamColumnInfoPo paramColumnInfoPo){
        paramColumnInfoPo.setCreateUserId((Integer) SecurityUtils.getSubject().getSession().getAttribute("userInfoId"));
        columnManageService.updateColumnManage(paramColumnInfoPo);
        return JsonView.render(200,"更新成功");
    }

    /**
     * 删除栏目信息
     * @param id 栏目ID
     * @return 返回响应
     */
    @RequestMapping(value = "/delete_column",method = RequestMethod.POST)
    public String deleteColumn(@RequestParam("column_id") int id){
        columnManageService.deleteColumnInfo(id);
        return JsonView.render(200,"删除成功");
    }

    /**
     * 通过栏目名称获取信息
     * @param columnName 栏目名称
     * @return 返回响应
     */
    @RequestMapping(value = "/get_parent_info_by_name",method = RequestMethod.GET)
    public String getParentNameByColumnName(@RequestParam("columnName")String columnName){
        List<ColumnInfoVo> rs = columnManageService.getColumnInfoByName(columnName);
        return  JsonView.render(200,"成功",rs);
    }

    /**
     * 通过父级栏目ID获取栏目集合
     * @param columnId 父级栏目ID
     * @return 返回响应
     */
    @RequestMapping(value = "/get_all_parent_column", method = RequestMethod.GET)
    public String getAllParentColumn(@RequestParam("column_id")String columnId){
        List<ColumnInfoVo> rs = columnManageService.getAllParentColumnList(columnId);
        return  JsonView.render(200,"成功",rs);
    }
}
