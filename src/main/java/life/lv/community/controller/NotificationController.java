package life.lv.community.controller;

import life.lv.community.dto.NotificationDTO;
import life.lv.community.enums.NotificationTypeEnum;
import life.lv.community.mapper.CommentMapper;
import life.lv.community.model.Comment;
import life.lv.community.model.User;
import life.lv.community.service.NotificationService;
import life.lv.community.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CommentMapper commentMapper;


    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()
        || NotificationTypeEnum.LIKE_QUESTION.getType()==notificationDTO.getType()
        ) {
            return "redirect:/question/" + notificationDTO.getOuterid();
        }else if(NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.LIKE_COMMENT.getType()==notificationDTO.getType()
        ){
            Comment dbComment=commentMapper.selectParentId(notificationDTO.getOuterid());
            return "redirect:/question/" + dbComment.getParentId();
        }
        else {
            return "redirect:/";
        }
    }
    @ResponseBody
    @GetMapping("/oneKeyRead")
    public Object oneKeyRead(HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        notificationService.readAll(user);
        return ResultVoUtil.success();
    }
    @ResponseBody
    @GetMapping("/deleteRead")
    public Object deleteRead(HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        notificationService.deleteRead(user);
        return ResultVoUtil.success();
    }


}
