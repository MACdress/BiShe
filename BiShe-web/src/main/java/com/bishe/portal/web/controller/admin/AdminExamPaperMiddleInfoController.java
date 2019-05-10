package com.bishe.portal.web.controller.admin;

import com.bishe.portal.model.vo.*;
import com.bishe.portal.service.ExamPaperMiddleInfoService;
import com.bishe.portal.web.utils.JsonView;
import com.qiniu.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/admin/examPaperMiddle")
public class AdminExamPaperMiddleInfoController {

    @Resource
    ExamPaperMiddleInfoService examPaperMiddleInfoService;

    @RequestMapping(value = "addCourseMiddleInfo",method = RequestMethod.POST)
    @ResponseBody
    public String addCourseMiddleInfo(@RequestBody AddExamPaperMiddleVo addExamPaperMiddleVo){
        if (addExamPaperMiddleVo == null){
            return JsonView.render(404,"入参为空");
        }
        examPaperMiddleInfoService.addQuestionInExamPaper(addExamPaperMiddleVo);
        return JsonView.render(200,"添加成功");
    }

    @RequestMapping(value = "addParamSelect",method = RequestMethod.POST)
    @ResponseBody
    public String addParamSelect(@RequestBody ExamSelectInfoVo selectInfoVo){
        examPaperMiddleInfoService.addParamSelect(selectInfoVo);
        return JsonView.render(200,"添加成功");
    }

    @RequestMapping(value = "addParamJudge",method = RequestMethod.POST)
    @ResponseBody
    public String addParamJudge(@RequestBody ExamJudgeInfoVo judgeInfoVo){
        examPaperMiddleInfoService.addParamJudge(judgeInfoVo);
        return JsonView.render(200,"添加成功");
    }

    @RequestMapping(value = "getExamPaperInfo",method = RequestMethod.GET)
    @ResponseBody
    public String getExamPaperInfo(String examPaperNum){
        ExamPaperMiddleInfoVo examPaperVo = examPaperMiddleInfoService.getExamPaperInfo(examPaperNum);
        return JsonView.render(200,"查询成功",examPaperVo);
    }

    @RequestMapping(value = "removeExamPaperSubject",method = RequestMethod.POST)
    @ResponseBody
    public String removeExamPaperSubject(@RequestBody AddExamPaperMiddleVo addExamPaperMiddleVo){
        if (addExamPaperMiddleVo==null){
            return JsonView.render(404,"入参错误");
        }
        String examPaperNum = addExamPaperMiddleVo.getExamPaperNum();
        List<SimpleSubjectVo> subjectIdList = addExamPaperMiddleVo.getSubjectList();
        if (StringUtils.isNullOrEmpty(examPaperNum)||(subjectIdList==null)||(subjectIdList.size()==0)){
            return JsonView.render(404,"入参错误");
        }
        examPaperMiddleInfoService.removeExamPaperSubject(examPaperNum,subjectIdList);
        return JsonView.render(200,"删除成功");
    }

    @RequestMapping(value = "getAllSelectSubject",method = RequestMethod.POST)
    @ResponseBody
    public String getAllSelectSubject(@RequestBody PageShowVo pageShowVo){
        if(pageShowVo==null){
            pageShowVo = new PageShowVo();
        }
        pageShowVo.setPage(pageShowVo.getPage()==null?1:pageShowVo.getPage());
        pageShowVo.setPageSize(pageShowVo.getPageSize()==null?20:pageShowVo.getPageSize());
        PageShowVo result = examPaperMiddleInfoService.getAllSelectExamPaperSubject(pageShowVo);
        return JsonView.render(200,"查询成功",result);
    }

    @RequestMapping(value = "getAllJudgeSubject",method = RequestMethod.POST)
    @ResponseBody
    public String getAllJudgeSubject(@RequestBody PageShowVo pageShowVo){
        if(pageShowVo==null){
            pageShowVo = new PageShowVo();
        }
        pageShowVo.setPage(pageShowVo.getPage()==null?1:pageShowVo.getPage());
        pageShowVo.setPageSize(pageShowVo.getPageSize()==null?20:pageShowVo.getPageSize());
        PageShowVo result = examPaperMiddleInfoService.getAllJudgeExamPaperSubject(pageShowVo);
        return JsonView.render(200,"查询成功",result);
    }
}
