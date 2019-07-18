package com.bishe.portal.web.controller.admin;

import com.bishe.portal.model.vo.QuestionnairePaperVo;
import com.bishe.portal.model.vo.QuestionnaireSelectVo;
import com.bishe.portal.service.AdminQuestionnaireService;
import com.bishe.portal.web.utils.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/admin/questionnaire")
public class AdminQuestionnaireController {
    @Resource
    AdminQuestionnaireService adminQuestionnaireService;

    @RequestMapping(value = "createQuestionnaire",method = RequestMethod.POST)
    @ResponseBody
    public String createQuestionnaire(@RequestBody QuestionnairePaperVo questionnaire){
        if ((questionnaire == null)||(questionnaire.getQuestionnaireName()==null)){
            return JsonView.render(404,"入参不完整");
        }
        QuestionnairePaperVo result =  adminQuestionnaireService.addQuestionnaire(questionnaire.getQuestionnaireName());
        return JsonView.render(200,"请求成功",result);
    }

    @RequestMapping(value = "addQuestion",method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestBody QuestionnaireSelectVo questionnaireSelectVo){
        if ((questionnaireSelectVo==null)||(questionnaireSelectVo.getQuestionnaireNum()==null)){
            return JsonView.render(404,"入参不完整");
        }
        QuestionnaireSelectVo result = adminQuestionnaireService.addQuestion(questionnaireSelectVo);
        return JsonView.render(200,"成功",result);
    }

    @RequestMapping(value = "removeQuestion",method = RequestMethod.POST)
    @ResponseBody
    public String removeQuestion(@RequestBody List<QuestionnaireSelectVo> questionnaireList){
        if ((questionnaireList==null)||(questionnaireList.size()==0)){
            return JsonView.render(404,"入参不完整");
        }
        adminQuestionnaireService.removeQuestion(questionnaireList);
        return JsonView.render(200,"成功");
    }

    @RequestMapping(value = "deleteQuestionnaire",method = RequestMethod.POST)
    @ResponseBody
    public String deleteQuestionnaire(@RequestBody QuestionnairePaperVo questionnaire){
        if ((questionnaire==null)||(questionnaire.getQuestionnaireNum()==null)){
            return JsonView.render(404,"入参不完整");
        }
        adminQuestionnaireService.deleteQuestionnaire(questionnaire.getQuestionnaireNum());
        return JsonView.render(200,"成功");
    }

    @RequestMapping(value = "getQuestionnaire",method = RequestMethod.GET)
    @ResponseBody
    public String getQuestionnaire(@RequestParam("questionnaireNum") String questionnaireNum,@RequestParam("page") Integer page, @RequestParam("pageSize")Integer pageSize){
        if (questionnaireNum == null){
            return JsonView.render(404,"入参不完整");
        }
        if((page == null)||(page == 0)){
            page = 1;
        }
        if ((pageSize==null)||(pageSize == 0)){
            pageSize = 20;
        }
        QuestionnairePaperVo result =  adminQuestionnaireService.getQuestionnaire(questionnaireNum, page, pageSize, "");
        return JsonView.render(200,"请求成功",result);
    }

    @RequestMapping(value = "getAllQuestionnaire",method = RequestMethod.GET)
    @ResponseBody
    public String getAllQuestionnaire(){
        List<QuestionnairePaperVo> result =  adminQuestionnaireService.getAllQuestionnaire();
        return JsonView.render(200,"请求成功",result);
    }
}
