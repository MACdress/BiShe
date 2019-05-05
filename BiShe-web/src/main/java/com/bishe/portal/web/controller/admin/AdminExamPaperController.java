package com.bishe.portal.web.controller.admin;

/**
 * @author 73515
 */

import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.model.vo.FindExamPaperVo;
import com.bishe.portal.service.ExamPaperService;
import com.bishe.portal.web.utils.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/admin/examPaper")
public class AdminExamPaperController {
    @Resource
    private  ExamPaperService examPaperService;

    @RequestMapping(value = "/add_examPaper",method = RequestMethod.POST)
    @ResponseBody
    public String addExamPaper(@RequestBody ExamPaperVo examPaperVo){
        examPaperService.addExamPaper(examPaperVo);
        return JsonView.render(200,"插入成功");
    }

    @RequestMapping(value = "/update_examPaper",method = RequestMethod.POST)
    @ResponseBody
    public String updateExamPaper(@RequestBody ExamPaperVo examPaperVo){
        examPaperService.updateExamPaper(examPaperVo);
        return JsonView.render(200,"更新成功");
    }

    @RequestMapping(value = "/delete_examPaper",method = RequestMethod.GET)
    @ResponseBody
    public String deleteExamPaper(@RequestParam("examPaperNum") String examPaperNum){
        examPaperService.deleteExamPaper(examPaperNum);
        return JsonView.render(200,"删除成功");
    }

    @RequestMapping(value = "/find_examPaper",method = RequestMethod.GET)
    @ResponseBody
    public String findExamPaperInfo(@RequestBody FindExamPaperVo findExamPaperVo){
        List<ExamPaperVo> result = examPaperService.findExamPaperList(findExamPaperVo);
        return JsonView.render(200,"查询成功",result);
    }
}
