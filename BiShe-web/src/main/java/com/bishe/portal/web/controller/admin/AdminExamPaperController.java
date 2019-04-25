package com.bishe.portal.web.controller.admin;

/**
 * @author 73515
 */

import com.bishe.portal.model.po.ParamColumnInfoPo;
import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.service.ExamPaperService;
import com.bishe.portal.web.utils.JsonView;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@CrossOrigin
@RequestMapping(value = "/admin/examPaper")
public class AdminExamPaperController {
    @Resource
    private  ExamPaperService examPaperService;

    @RequestMapping(value = "/add_examPaper",method = RequestMethod.POST)
    public String addExamPaper(@RequestBody ExamPaperVo examPaperVo){
        examPaperService.addExamPaper(examPaperVo);
        return JsonView.render(200,"插入成功");
    }

    @RequestMapping(value = "/update_examPaper",method = RequestMethod.POST)
    public String updateExamPaper(@RequestBody ExamPaperVo examPaperVo){
        examPaperService.updateExamPaper(examPaperVo);
        return JsonView.render(200,"更新成功");
    }

    @RequestMapping(value = "/delete_examPaper",method = RequestMethod.GET)
    public String deleteExamPaper(@RequestParam("examPaperId") int examPaperId){
        examPaperService.deleteExamPaper(examPaperId);
        return JsonView.render(200,"更新成功");
    }
}
