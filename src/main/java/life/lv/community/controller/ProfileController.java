package life.lv.community.controller;

import life.lv.community.VO.ResultVO;
import life.lv.community.dto.PageinationDTO;
import life.lv.community.model.User;
import life.lv.community.service.NotificationService;
import life.lv.community.service.QuestionService;
import life.lv.community.service.UserService;
import life.lv.community.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{id}")
    public String profile(
                          Model model, HttpServletRequest request,
                          @PathVariable("id") Long id
                         ){
           User userSelect = userService.findById(id);

           model.addAttribute("user",userSelect);
           return "profile";

    }
    @GetMapping("/replies")
    public String replies(
//            @PathVariable("action") String action,
                          Model model, HttpServletRequest request,
                          @RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "pageNum",defaultValue = "5") Integer pageNum){
        User user=(User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
//        if("replies".equals(action)){
            String action="replies";
            //通过action 查询是通知还是个人动态 dynamic 个人动态 reolies 通知
            PageinationDTO pageinationDTO = notificationService.listUser(user.getId(), page, pageNum,action);
            model.addAttribute("pageination", pageinationDTO);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
//        }
//        if("follow".equals(action)){
//            model.addAttribute("section","follow");
//            model.addAttribute("sectionName","最新回复");
//        }
//        if("collection".equals(action)){
//            model.addAttribute("section","collection");
//            model.addAttribute("sectionName","最新回复");
//        }
        return "replies";
    }
    @ResponseBody
    @GetMapping("/loadUserData/{action}")
    public Object loadUserData(@PathVariable("action") String action,
                               @RequestParam(value = "userId") Long userId,
                               @RequestParam(value = "page",defaultValue = "1") Integer page,
                               @RequestParam(value = "pageNum",defaultValue = "5") Integer pageNum){
        User user = userService.findById(userId);
        if(user==null){
            return ResultVoUtil.error(1007,"用户不存在");
        }else {
            if("dynamic".equals(action)){
                //加载个人最新动态
                //可通过通知表实现 查出通知者的id
                //通过action 查询是通知还是个人动态 dynamic 个人动态 reolies 通知
                PageinationDTO pageinationDTO = notificationService.listUser(user.getId(), page, pageNum, action);
                ResultVO resultVO=new ResultVO();
                resultVO.setCode(200);
                resultVO.setMessage("成功");
                resultVO.setData(pageinationDTO);
                return resultVO;
            }else if("question".equals(action)){
                //加载个人问题
                PageinationDTO pageinationDTO=questionService.listUser(user.getId(),page,pageNum);
                ResultVO resultVO=new ResultVO();
                resultVO.setCode(200);
                resultVO.setMessage("成功");
                resultVO.setData(pageinationDTO);
                return resultVO;

            }else if("collection".equals(action)){
                //加载个人收藏
            }else if("follow".equals(action)){
                //加载个人关注
            }else {
                //加载个人粉丝
            }
        }




        return null;
    }







//    @GetMapping("/profile/updatePersonal")
//    public String updatePersonal(User user){
//        userService.updateUser(user);
//
//        return "redirect:/profile/personal";
//    }
}
