package life.lv.community.controller;

import life.lv.community.dto.CommentDTO;
import life.lv.community.dto.QuestionDTO;
import life.lv.community.enums.CommentTypeEnum;
import life.lv.community.service.CommentService;
import life.lv.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model){
        QuestionDTO questionDTO=questionService.getById(id);
        List<QuestionDTO> relateQuestions=questionService.questionByTagList(questionDTO);
        List<CommentDTO> commentDTOList=commentService.listByQuestionId(id, CommentTypeEnum.QUESTION.getType());


        //增加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOList);
        model.addAttribute("relateQuestions",relateQuestions);
        return "question";
    }

}
