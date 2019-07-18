package com.bishe.portal.web.controller.admin;

/**
 * @author 73515
 */

import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.model.vo.FindExamPaperVo;
import com.bishe.portal.model.vo.ResultExamPaperVo;
import com.bishe.portal.service.ExamPaperService;
import com.bishe.portal.web.utils.JsonView;
import com.google.gson.JsonObject;
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
        ExamPaperVo examPaperVo1 = examPaperService.addExamPaper(examPaperVo);
        return JsonView.render(200,"插入成功",examPaperVo1);
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

    @RequestMapping(value = "/find_examPaper",method = RequestMethod.POST)
    @ResponseBody
    public String findExamPaperInfo(@RequestBody FindExamPaperVo findExamPaperVo){
        if (findExamPaperVo.getPage() == null ||findExamPaperVo.getPageSize()==null){
            findExamPaperVo.setPage(1);
            findExamPaperVo.setPageSize(20);
        }
        List<ExamPaperVo> result = examPaperService.findExamPaperList(findExamPaperVo);
        ResultExamPaperVo examPaperVo = new ResultExamPaperVo();
        examPaperVo.setResult(result);
        examPaperVo.setPage(findExamPaperVo.getPage());
        examPaperVo.setPageSize(findExamPaperVo.getPageSize());
        return JsonView.render(200,"查询成功",examPaperVo);
    }

//    @RequestMapping(value = "/find_examPaperByType",method = RequestMethod.GET)
//    @ResponseBody
//    public String findExamPaperByType (Integer examPaperType,Integer page,Integer pageSize){
//        if ((examPaperType == null)||(examPaperType == 0)||(page == null)||(pageSize == null)){
//            return JsonView.render(404,"入参有误");
//        }
//        int pageNum = page == null ? 1:page;
//        int pageSizeNum = pageSize == null?0:pageSize;
//        List<ExamPaperVo> result = examPaperService.findExamPaperListByType(examPaperType,pageNum,pageSizeNum);
//        ResultExamPaperVo examPaperVo = new ResultExamPaperVo();
//        examPaperVo.setResult(result);
//        examPaperVo.setPage(page);
//        examPaperVo.setPageSize(pageSize);
//        return JsonView.render(200,"查询成功",examPaperVo);
//    }
}
