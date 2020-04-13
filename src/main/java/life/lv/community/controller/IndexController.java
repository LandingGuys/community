package life.lv.community.controller;

import life.lv.community.VO.ResultVO;
import life.lv.community.cache.HotTagCache;
import life.lv.community.dto.PageinationDTO;
import life.lv.community.model.User;
import life.lv.community.service.QuestionService;
import life.lv.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Slf4j
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private HotTagCache hotTagCache;
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "pageNum",defaultValue = "15") Integer pageNum,
                        @RequestParam(value = "search",required = false) String search,
                        @RequestParam(value = "tag",required = false) String tag,
                        @RequestParam(value = "sort",required = false) String sort
                       ){

        PageinationDTO pageinationDTO  =questionService.list(search,tag,sort,page,pageNum);


        model.addAttribute("pageination",pageinationDTO);
        model.addAttribute("search",search);
        model.addAttribute("tag", tag);
        model.addAttribute("sort", sort);
        return "index";
    }
    @ResponseBody
    @GetMapping("/loadIndex")
    public Object loadIndex(@RequestParam(value = "page",defaultValue = "1") Integer page,
                            @RequestParam(value = "pageNum",defaultValue = "15") Integer pageNum,
                            @RequestParam(value = "search",required = false) String search,
                            @RequestParam(value = "tag",required = false) String tag,
                            @RequestParam(value = "sort",required = false) String sort){

        PageinationDTO pageinationDTO  =questionService.list(search,tag,sort,page,pageNum);
        ResultVO resultVO=new ResultVO();
        resultVO.setCode(200);
        resultVO.setMessage("成功");
        resultVO.setData(pageinationDTO);
        return resultVO;
    }
    @ResponseBody
    @GetMapping("/loadRightList")
    public Object loadRightList(){
        //最新用户
        List<User> userNewList = userService.findNewUsers(12);
        //最热问题
        PageinationDTO recommendQuestions = questionService.list(null,null,"hot",1,10);

        List<String> tags=hotTagCache.getHots();

        ResultVO resultVO=new ResultVO();
        resultVO.setCode(200);
        resultVO.setMessage("成功");
        resultVO.addMsg("userNewList",userNewList)
                .addMsg("recommend",recommendQuestions)
                .addMsg("tags",tags);
        return resultVO;
    }



}
