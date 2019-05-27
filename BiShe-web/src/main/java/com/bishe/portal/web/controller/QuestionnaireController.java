package com.bishe.portal.web.controller;

import com.bishe.portal.model.vo.PageChooseQuestion;
import com.bishe.portal.model.vo.QuestionnairePaperVo;
import com.bishe.portal.model.vo.UserInfoVo;
import com.bishe.portal.service.AdminQuestionnaireService;
import com.bishe.portal.web.utils.JsonView;
import com.qiniu.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/questionnaire")
public class QuestionnaireController {
    @Resource
    AdminQuestionnaireService adminQuestionnaireService;
    @RequestMapping(value = "getQuestionnaire",method = RequestMethod.GET)
    @ResponseBody
    public String getQuestionnaire(@RequestParam("questionnaireNum") String questionnaireNum, @RequestParam("page") Integer page, @RequestParam("pageSize")Integer pageSize,HttpSession session){
        if((page == null)||(page == 0)){
            page = 1;
        }
        if ((pageSize==null)||(pageSize == 0)){
            pageSize = 20;
        }
        UserInfoVo userInfoVo = (UserInfoVo)session.getAttribute("user");
        if (userInfoVo == null){
            return JsonView.render(404,"user is not admin");
        }
        if (questionnaireNum == null){
            return JsonView.render(404,"入参不完整");
        }
        QuestionnairePaperVo result =  adminQuestionnaireService.getQuestionnaire(questionnaireNum,page,pageSize,userInfoVo.getAccount());
        return JsonView.render(200,"请求成功",result);
    }

    @RequestMapping(value = "getAllQuestionnaire",method = RequestMethod.GET)
    @ResponseBody
    public String getAllQuestionnaire(){
        List<QuestionnairePaperVo> result =  adminQuestionnaireService.getAllQuestionnaire();
        return JsonView.render(200,"请求成功",result);
    }

    @RequestMapping(value = "pageChooseQuestion",method = RequestMethod.POST)
    @ResponseBody
    public String pageChooseQuestion(@RequestBody PageChooseQuestion pageChooseQuestion,HttpSession session){
        UserInfoVo userInfoVo = (UserInfoVo)session.getAttribute("user");
        if (userInfoVo == null){
            return JsonView.render(404,"user is not admin");
        }
        if (StringUtils.isNullOrEmpty(pageChooseQuestion.getQuestionnaireNum())||(pageChooseQuestion.getAnswers()==null)){
            return JsonView.render(404,"入参错误");
        }
        adminQuestionnaireService.chooseQuestion(pageChooseQuestion.getAnswers(),pageChooseQuestion.getQuestionnaireNum(),userInfoVo.getAccount());
        return JsonView.render(200,"选择成功");
    }

    @RequestMapping(value = "getUserQuestion",method = RequestMethod.GET)
    @ResponseBody
    public String getUserQuestion(HttpSession session){
        UserInfoVo userInfoVo = (UserInfoVo)session.getAttribute("user");
        if (userInfoVo == null){
            return JsonView.render(404,"user is not admin");
        }
        List<QuestionnairePaperVo>result = adminQuestionnaireService.getUserQuestion(userInfoVo.getAccount());
        return JsonView.render(200,"选择成功",result);
    }
    @RequestMapping(value = "startAnswerQuestion",method = RequestMethod.GET)
    @ResponseBody
    public String startAnswerQuestion(@RequestParam("questionnaireNum")String questionnaireNum, HttpSession session){
        UserInfoVo userInfoVo = (UserInfoVo)session.getAttribute("user");
        if (userInfoVo == null){
            return JsonView.render(404,"user is not admin");
        }
        adminQuestionnaireService.startAnswerQuestion(questionnaireNum,userInfoVo.getAccount());
        return JsonView.render(200,"成功");
    }
}
