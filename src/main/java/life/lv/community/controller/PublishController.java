package life.lv.community.controller;

import life.lv.community.cache.TagCache;
import life.lv.community.dto.QuestionDTO;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.UserMapper;
import life.lv.community.model.Question;
import life.lv.community.model.User;
import life.lv.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam("id") Long id,
                            HttpServletRequest request,
                            Model model) {

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(title==null || title.trim().length()==0){
            model.addAttribute("error", "标题不能为空，只有空格也不行哦！");
            return "publish";
        }
        if(description==null||description.trim().length()==0){
            model.addAttribute("error", "内容不能为空，只有空格也不行哦！");
            return "publish";
        }
        if(tag==null||tag.trim().length()==0){
            model.addAttribute("error", "标签不能为空，只有空格也不行哦！");
            return "publish";
        }
        //判断标签是否非法
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }
        //判断用户是否为空
        User user=(User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Long id,
                       HttpServletRequest request,
                       Model model){
        User user=(User)request.getSession().getAttribute("user");
        QuestionDTO question=questionService.getById(id);
        User publishUser=userMapper.findById(question.getCreator());
        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.EDIT_QUEDTION_NOTLOGIN);
        }
        if(user.equals(publishUser)){
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            model.addAttribute("id",question.getId());
            model.addAttribute("tags",TagCache.get());
            return "publish";

        }else {
            throw new CustomizeException(CustomizeErrorCode.EDIT_QUESTION_FAIL);
        }

    }

    
}
