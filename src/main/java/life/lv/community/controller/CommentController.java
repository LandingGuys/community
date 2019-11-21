package life.lv.community.controller;

import life.lv.community.VO.LikeVO;
import life.lv.community.VO.ResultVO;
import life.lv.community.dto.CommentDTO;
import life.lv.community.dto.CommetCreateDTO;
import life.lv.community.enums.CommentTypeEnum;
import life.lv.community.enums.NotificationStatusEnum;
import life.lv.community.enums.NotificationTypeEnum;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.CommentExtMapper;
import life.lv.community.mapper.CommentMapper;
import life.lv.community.mapper.NotificationMapper;
import life.lv.community.model.Comment;
import life.lv.community.model.Notification;
import life.lv.community.model.User;
import life.lv.community.service.CommentService;
import life.lv.community.service.QuestionService;
import life.lv.community.utils.ResultVoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private QuestionService questionService;

    /**
     * 评论
     * @param commetCreateDTO
     * @param request
     * @return
     */
    @PostMapping("/comment")
    public Object post(@RequestBody CommetCreateDTO commetCreateDTO,
                       HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if(user == null){
            return ResultVoUtil.error(CustomizeErrorCode.NO_LOGIN.getCode(),CustomizeErrorCode.NO_LOGIN.getMessage());
        }
        if(commetCreateDTO==null|| StringUtils.isBlank(commetCreateDTO.getContent())){
            return ResultVoUtil.error(CustomizeErrorCode.CONTENT_IS_EMPTY.getCode(),CustomizeErrorCode.CONTENT_IS_EMPTY.getMessage());
        }
        Comment comment = new Comment();
        comment.setParentId(commetCreateDTO.getParentId());
        comment.setContent(commetCreateDTO.getContent());
        comment.setType(commetCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentor(user.getId());
        commentService.insert(comment,user);
        return ResultVoUtil.success();
    }


    @GetMapping("/comment/{id}")
    public ResultVO<List> comment(@PathVariable("id") Long id){
        List<CommentDTO> commentDTOList=commentService.listByQuestionId(id, CommentTypeEnum.COMMENT.getType());
        return ResultVoUtil.success(commentDTOList);
    }

    /**
     * 点赞评论
     * @param id
     * @param request
     * @return
     */
    @ResponseBody()
    @PostMapping("/likeComment")
    public Object likeComment(@RequestParam("id") long id,
                               HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if(user == null){
            return ResultVoUtil.error(CustomizeErrorCode.NO_LOGIN.getCode(),CustomizeErrorCode.NO_LOGIN.getMessage());
        }
        Comment dbComment = commentMapper.selectByPrimaryKey(id);
        //Comment dbComment=commentMapper.selectParentId(id);
        if(dbComment==null){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
        }
        if(!user.getId().equals(dbComment.getCommentor())){
            String res=commentService.incLike(id,user.getId());
            if("success".equals(res)){
                //通知
                Notification notification = new Notification();
                notification.setGmtCreate(System.currentTimeMillis());
                notification.setType(NotificationTypeEnum.LIKE_COMMENT.getType());
                notification.setOuterId(id);
                notification.setNotifier(user.getId());
                notification.setReceiver(dbComment.getCommentor());
                notification.setNotifierName(user.getName());
                notification.setOuterTitle(dbComment.getContent());
                notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
                notificationMapper.insert(notification);
            }else{
                return ResultVoUtil.error(CustomizeErrorCode.REPEAT_LIKE.getCode(),CustomizeErrorCode.REPEAT_LIKE.getMessage());
            }



        }else{
            return ResultVoUtil.error(CustomizeErrorCode.CANT_LIKE_YOURSELF_QUESTION.getCode(),CustomizeErrorCode.CANT_LIKE_YOURSELF_QUESTION.getMessage());
        }
        Comment likeComment=commentMapper.selectByPrimaryKey(id);
        //Comment likeComment =commentMapper.selectParentId(id);
        LikeVO likeVO=new LikeVO();
        likeVO.setLikeCount(likeComment.getLikeCount());
        return ResultVoUtil.success(likeVO);
    }


}
