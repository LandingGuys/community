package life.lv.community.controller;

import life.lv.community.cache.TagCache;
import life.lv.community.dto.PublishDTO;
import life.lv.community.dto.QuestionDTO;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.UserMapper;
import life.lv.community.model.QuestionWithBLOBs;
import life.lv.community.model.User;
import life.lv.community.service.QuestionService;
import life.lv.community.utils.ResultVoUtil;
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
    @ResponseBody()
    @PostMapping("/publish")
    public Object doPublish(@RequestBody PublishDTO publishDTO,
                            HttpServletRequest request,
                            Model model) {
        String title=publishDTO.getTitle();
        String description=publishDTO.getDescription();
        String tag=publishDTO.getTag();
        Long id=publishDTO.getId();
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(title==null || title.trim().length()==0){
            //model.addAttribute("error", "标题不能为空，只有空格也不行哦！");
            return ResultVoUtil.error(CustomizeErrorCode.TITLE_IS_EMPTY.getCode(),CustomizeErrorCode.TITLE_IS_EMPTY.getMessage());
        }
        if(description==null||description.trim().length()==0){
           // model.addAttribute("error", "内容不能为空，只有空格也不行哦！");
            return ResultVoUtil.error(CustomizeErrorCode.DESCRIPTION_IS_EMPTY.getCode(),CustomizeErrorCode.DESCRIPTION_IS_EMPTY.getMessage());
        }
        if(tag==null||tag.trim().length()==0){
            //model.addAttribute("error", "标签不能为空，只有空格也不行哦！");
            return ResultVoUtil.error(CustomizeErrorCode.TAG_IS_EMPTY.getCode(),CustomizeErrorCode.TAG_IS_EMPTY.getMessage());
        }
        //判断标签是否非法
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            //model.addAttribute("error", "输入非法标签:" + invalid);
            return ResultVoUtil.error(CustomizeErrorCode.TAG_IS_WRONGFUL.getCode(),CustomizeErrorCode.TAG_IS_WRONGFUL.getMessage());
        }
        //判断用户是否为空
        User user=(User) request.getSession().getAttribute("user");
        if (user == null) {
            //model.addAttribute("error", "用户未登录");
            return ResultVoUtil.error(CustomizeErrorCode.NO_LOGIN.getCode(),CustomizeErrorCode.NO_LOGIN.getMessage());
        }
        QuestionWithBLOBs question = new QuestionWithBLOBs();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return ResultVoUtil.success();
    }
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Long id,
                       HttpServletRequest request,
                       Model model){
        User user=(User)request.getSession().getAttribute("user");
        QuestionDTO question=questionService.getById(id);
        User publishUser=userMapper.selectByPrimaryKey(question.getCreator());
        //User publishUser=userMapper.findById(question.getCreator());
        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.EDIT_QUEDTION_NOTLOGIN);
        }
        if(user.getId().equals(publishUser.getId())){
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
