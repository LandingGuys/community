package life.lv.community.controller;

import life.lv.community.dto.PageinationDTO;
import life.lv.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "pageNum",defaultValue = "10") Integer pageNum,
                        @RequestParam(value = "search",required = false) String search
                       ){

        PageinationDTO pageinationDTO  =questionService.list(search,page,pageNum);

        model.addAttribute("pageination",pageinationDTO);
        model.addAttribute("search",search);
        return "index";
    }



}
