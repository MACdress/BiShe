package com.bishe.portal.web.controller.admin;

import com.bishe.portal.web.utils.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping(value = "/admin/cost")
public class AdminUserCostInfoController {
    @RequestMapping(value = "getUserCostInfo",method = RequestMethod.POST)
    @ResponseBody
    public String getUserCostInfo(){
        return JsonView.render(200,"查询成功");
    }

}
