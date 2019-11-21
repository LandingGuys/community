package life.lv.community.controller;

import life.lv.community.cache.HotTagCache;
import life.lv.community.dto.PageinationDTO;
import life.lv.community.service.QuestionService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Slf4j
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "pageNum",defaultValue = "20") Integer pageNum,
                        @RequestParam(value = "search",required = false) String search,
                        @RequestParam(value = "tag",required = false) String tag,
                        @RequestParam(value = "sort",required = false) String sort
                       ){

        PageinationDTO pageinationDTO  =questionService.list(search,tag,sort,page,pageNum);

        List<String> tags=hotTagCache.getHots();
        model.addAttribute("pageination",pageinationDTO);
        model.addAttribute("search",search);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tags);
        model.addAttribute("sort", sort);
        return "index";
    }



}
