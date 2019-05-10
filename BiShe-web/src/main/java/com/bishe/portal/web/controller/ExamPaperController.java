package com.bishe.portal.web.controller;

import com.bishe.portal.model.vo.*;
import com.bishe.portal.service.ExamPaperService;
import com.bishe.portal.service.ExamStartService;
import com.bishe.portal.web.utils.JsonView;
import com.qiniu.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/examPaper")
public class ExamPaperController {
    @Resource
    ExamPaperService examPaperService;
    @Resource
    ExamStartService examStartService;

    @RequestMapping(value = "getExamPaper",method = RequestMethod.GET)
    @ResponseBody
    public String getExamPaper(HttpSession session){
        UserInfoVo tbUsersPo  = (UserInfoVo) session.getAttribute("user");
        if ((tbUsersPo == null)||(tbUsersPo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        List<ExamPaperVo>examPaperVoList =  examPaperService.getReleaseExamPaper(tbUsersPo.getAccount());
        return JsonView.render(200,"查询成功",examPaperVoList);
    }

    @RequestMapping(value = "startExam",method = RequestMethod.GET)
    @ResponseBody
    public String startExam(String examPaperNum, HttpSession session){
        if (StringUtils.isNullOrEmpty(examPaperNum)){
            return JsonView.render(404,"入参错误");
        }
        UserInfoVo tbUsersPo  = (UserInfoVo) session.getAttribute("user");
        if ((tbUsersPo == null)||(tbUsersPo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        ExamPaperStartMiddleInfoVo examStartVo =  examStartService.startExam(examPaperNum,tbUsersPo.getAccount());
        return  JsonView.render(200,"成功",examStartVo);
    }

    @RequestMapping(value = "endExam",method = RequestMethod.GET)
    @ResponseBody
    public String endExam(String examStartNum,HttpSession session){
        UserInfoVo tbUsersPo  = (UserInfoVo) session.getAttribute("user");
        if ((tbUsersPo == null)||(tbUsersPo.getPermission()!=1)){
            return JsonView.render(404,"user is not admin");
        }
        if (StringUtils.isNullOrEmpty(examStartNum)){
            return JsonView.render(404,"入参错误");
        }
        ExamStartVo examStartVo =  examStartService.endExam(examStartNum);
        return  JsonView.render(200,"成功",examStartVo);
    }

    @RequestMapping(value = "chooseExamSubject",method = RequestMethod.POST)
    @ResponseBody
    public String chooseExamSubject(@RequestBody ExamChooseVo examChooseVo) {
        examStartService.chooseExamSubject(examChooseVo);
        return JsonView.render(200,"选择成功");
    }

    @RequestMapping(value = "pageChooseExamSubject",method = RequestMethod.POST)
    @ResponseBody
    public String pageChooseExamSubject(@RequestBody PageChooseExamVo pageChooseExamVo){
        if (StringUtils.isNullOrEmpty(pageChooseExamVo.getExamStartNum())||(pageChooseExamVo.getExamChooseList()==null)){
            return JsonView.render(404,"入参错误");
        }
        examStartService.ChooseExamSubjectList(pageChooseExamVo.getExamChooseList(),pageChooseExamVo.getExamStartNum());
        return JsonView.render(200,"选择成功");
    }

    @RequestMapping(value = "getAllExamHistory",method = RequestMethod.GET)
    @ResponseBody
    public String getAllExamHistory(@RequestParam("page")Integer page,@RequestParam("pageSize") Integer pageSize){
        if (page==null||pageSize==null){
            page = 1;
            pageSize = 20;
        }
        System.out.println(page);
        System.out.println(pageSize);
        List<ExamStartVo> examStartVoList = examStartService.getAllExamHistory(page,pageSize);
        return JsonView.render(200,"查询成功",examStartVoList);
    }
}
