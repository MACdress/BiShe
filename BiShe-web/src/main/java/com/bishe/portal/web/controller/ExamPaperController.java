package com.bishe.portal.web.controller;

import com.bishe.portal.model.vo.ExamChooseVo;
import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.model.vo.ExamStartVo;
import com.bishe.portal.model.vo.UserInfoVo;
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
    public String getExamPaper(){
        List<ExamPaperVo>examPaperVoList =  examPaperService.getReleaseExamPaper();
        return JsonView.render(200,"查询成功",examPaperVoList);
    }

    @RequestMapping(value = "startExam",method = RequestMethod.GET)
    @ResponseBody
    public String startExam(String examPaperNum, HttpSession session){
        if (StringUtils.isNullOrEmpty(examPaperNum)){
            return JsonView.render(404,"入参错误");
        }
        UserInfoVo tbUsersPo  = (UserInfoVo) session.getAttribute("user");
        ExamStartVo examStartVo =  examStartService.startExam(examPaperNum,tbUsersPo.getAccount());
        return  JsonView.render(200,"成功",examStartVo);
    }

    @RequestMapping(value = "endExam",method = RequestMethod.GET)
    @ResponseBody
    public String endExam(String examStartNum){
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
}
