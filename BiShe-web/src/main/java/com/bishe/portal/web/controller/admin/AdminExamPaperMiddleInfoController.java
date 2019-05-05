package com.bishe.portal.web.controller.admin;

import com.bishe.portal.model.vo.ExamJudgeInfoVo;
import com.bishe.portal.model.vo.ExamPaperMiddleInfoVo;
import com.bishe.portal.model.vo.ExamSelectInfoVo;
import com.bishe.portal.service.ExamPaperMiddleInfoService;
import com.bishe.portal.web.utils.JsonView;
import com.qiniu.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/admin/examPaperMiddle")
public class AdminExamPaperMiddleInfoController {

    @Resource
    ExamPaperMiddleInfoService examPaperMiddleInfoService;

    @RequestMapping(value = "addCourseMiddleInfo",method = RequestMethod.POST)
    @ResponseBody
    public String addCourseMiddleInfo(HttpSession session){
        List<ExamSelectInfoVo> examSelectList = (List<ExamSelectInfoVo>) session.getAttribute("examSelectList");
        List<ExamJudgeInfoVo> judgeInfoList = (List<ExamJudgeInfoVo>) session.getAttribute("judgeInfoList");
        examPaperMiddleInfoService.addQuestionInExamPaper(examSelectList,judgeInfoList);
        session.removeAttribute("examSelectList");
        session.removeAttribute("judgeInfoList");
        return JsonView.render(200,"添加成功");
    }

    @RequestMapping(value = "addParamSelect",method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestBody ExamSelectInfoVo selectInfoVo,HttpSession session){
        if (selectInfoVo.getExamPaperNum()==null){
            return JsonView.render(401,"入参错误");
        }
        List<ExamSelectInfoVo> examSelectList = (List<ExamSelectInfoVo>) session.getAttribute("examSelectList");
        /* Session 记录非空验证 */
        //第一次添加
        if (examSelectList == null) {
            examSelectList = new ArrayList<>();
        }
            //存在就移除,反之添加
        if (examSelectList.contains(selectInfoVo)==false) {
            examSelectList.add(selectInfoVo);
        }
        session.setAttribute("examQuestionList",examSelectList);
        return JsonView.render(200,"添加成功");
    }

    @RequestMapping(value = "addParamJudge",method = RequestMethod.POST)
    @ResponseBody
    public String addParamJudge(@RequestBody ExamJudgeInfoVo judgeInfoVo, HttpSession session){
        if (judgeInfoVo.getExamPaperNum()==null){
            return JsonView.render(401,"入参错误");
        }
        List<ExamJudgeInfoVo> judgeInfoList = (List<ExamJudgeInfoVo>) session.getAttribute("judgeInfoList");
        /* Session 记录非空验证 */
        //第一次添加
        if (judgeInfoList == null) {
            judgeInfoList = new ArrayList<>();
        }
        //存在就移除,反之添加
        if (!judgeInfoList.contains(judgeInfoVo)) {
            judgeInfoList.add(judgeInfoVo);
        }
        session.setAttribute("examQuestionList",judgeInfoList);
        return JsonView.render(200,"添加成功");
    }

    @RequestMapping(value = "clearSession",method = RequestMethod.GET)
    @ResponseBody
    public String isClearChooseSubjectIds(HttpSession session) {
        session.removeAttribute("examSelectList");
        session.removeAttribute("judgeInfoList");
        return JsonView.render(200,"移除成功");
    }

    @RequestMapping(value = "getExamPaperInfo",method = RequestMethod.GET)
    @ResponseBody
    public String getExamPaperInfo(String examPaperNum){
        ExamPaperMiddleInfoVo examPaperVo = examPaperMiddleInfoService.getExamPaperInfo(examPaperNum);
        return JsonView.render(200,"查询成功",examPaperVo);
    }

    @RequestMapping(value = "removeExamPaperSubject",method = RequestMethod.POST)
    @ResponseBody
    public String removeExamPaperSubject(@RequestParam("examPaperNum") String examPaperNum,@RequestParam("subjectId") String subjectId){
        if (StringUtils.isNullOrEmpty(examPaperNum)||StringUtils.isNullOrEmpty(subjectId)){
            return JsonView.render(404,"入参错误");
        }
        examPaperMiddleInfoService.removeExamPaperSubject(examPaperNum,subjectId);
        return JsonView.render(200,"删除成功");
    }
}
