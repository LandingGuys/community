package life.lv.community.controller;

import life.lv.community.dto.PageinationDTO;
import life.lv.community.model.User;
import life.lv.community.service.NotificationService;
import life.lv.community.service.QuestionService;
import life.lv.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller

public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action,
                          Model model, HttpServletRequest request,
                          @RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "pageNum",defaultValue = "5") Integer pageNum){
       User user=(User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        if("personal".equals(action)){
            model.addAttribute("section","personal");
            model.addAttribute("sectionName","个人中心");

        }
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PageinationDTO pageinationDTO=questionService.listUser(user.getId(),page,pageNum);
            model.addAttribute("pageination",pageinationDTO);
        }
        if("replies".equals(action)){
            PageinationDTO pageinationDTO = notificationService.listUser(user.getId(), page, pageNum);
            model.addAttribute("pageination", pageinationDTO);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        if("follow".equals(action)){
            model.addAttribute("section","follow");
            model.addAttribute("sectionName","最新回复");
        }
        if("collection".equals(action)){
            model.addAttribute("section","collection");
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
    @GetMapping("/profile/updatePersonal")
    public String updatePersonal(User user){
        userService.updateUser(user);

        return "redirect:/profile/personal";
    }
}
