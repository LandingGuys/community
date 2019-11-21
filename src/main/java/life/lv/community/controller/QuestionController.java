package life.lv.community.controller;

import life.lv.community.VO.LikeVO;
import life.lv.community.dto.CommentDTO;
import life.lv.community.dto.QuestionDTO;
import life.lv.community.enums.CommentTypeEnum;
import life.lv.community.enums.NotificationStatusEnum;
import life.lv.community.enums.NotificationTypeEnum;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.NotificationMapper;
import life.lv.community.mapper.QuestionMapper;
import life.lv.community.model.Notification;
import life.lv.community.model.Question;
import life.lv.community.model.User;
import life.lv.community.service.CommentService;
import life.lv.community.service.QuestionService;
import life.lv.community.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private QuestionMapper questionMapper;
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

    @ResponseBody()
    @PostMapping("/likeQuestion")
    public Object likeQuestion(@RequestParam("id") long id,
                               HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if(user == null){
            return ResultVoUtil.error(CustomizeErrorCode.NO_LOGIN.getCode(),CustomizeErrorCode.NO_LOGIN.getMessage());
        }
        Question dbQuestion = questionMapper.selectByPrimaryKey(id);
        //Question dbQuestion=questionMapper.getById(id);
        if(dbQuestion==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        if(user.getId()!=dbQuestion.getCreator()){

            String res=questionService.incLike(id,user.getId());
            if("success".equals(res)){
                //通知
                Notification notification = new Notification();
                notification.setGmtCreate(System.currentTimeMillis());
                notification.setType(NotificationTypeEnum.LIKE_QUESTION.getType());
                notification.setOuterId(id);
                notification.setNotifier(user.getId());
                notification.setReceiver(dbQuestion.getCreator());
                notification.setNotifierName(user.getName());
                notification.setOuterTitle(dbQuestion.getTitle());
                notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
                notificationMapper.insertSelective(notification);
            }else{
                return ResultVoUtil.error(CustomizeErrorCode.REPEAT_LIKE.getCode(),CustomizeErrorCode.REPEAT_LIKE.getMessage());
            }

            //notificationMapper.create(notification);

        }else{
            return ResultVoUtil.error(CustomizeErrorCode.CANT_LIKE_YOURSELF_QUESTION.getCode(),CustomizeErrorCode.CANT_LIKE_YOURSELF_QUESTION.getMessage());
        }
        Question likeQuestion =questionMapper.selectByPrimaryKey(id);
        //Question likeQuestion =questionMapper.getById(id);
        LikeVO likeVO=new LikeVO();
        likeVO.setLikeCount(likeQuestion.getLikeCount());
        return ResultVoUtil.success(likeVO);
    }
}
