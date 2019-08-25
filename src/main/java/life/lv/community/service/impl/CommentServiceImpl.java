package life.lv.community.service.impl;

import life.lv.community.dto.CommentDTO;
import life.lv.community.enums.CommentTypeEnum;
import life.lv.community.enums.NotificationStatusEnum;
import life.lv.community.enums.NotificationTypeEnum;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.CommentMapper;
import life.lv.community.mapper.NotificationMapper;
import life.lv.community.mapper.QuestionMapper;
import life.lv.community.mapper.UserMapper;
import life.lv.community.model.Comment;
import life.lv.community.model.Notification;
import life.lv.community.model.Question;
import life.lv.community.model.User;
import life.lv.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Transactional
    @Override
    public void insert(Comment comment,User commentator) {
        if(comment.getParentId() ==null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND );
        }
        if(comment.getType() ==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            //1.查出数据库有没有该评论
            Comment dbComment=commentMapper.selectParentId(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.getById(dbComment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            commentMapper.updateComment(dbComment);
            //Notification notification=CreateNotifyUtil.createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), comment.getContent(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
            //notificationMapper.create(notification);
        }else {
            //回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.updateComment(question);
            //Notification notification=CreateNotifyUtil.createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
            //notificationMapper.create(notification);
        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        //如果评论人是发布人则不通知
        if (receiver == comment.getCommentator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setNotifier(comment.getCommentator());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifierName(notifierName);
        notification.setOuterid(outerId);
        notification.setOuterTitle(outerTitle);
        notification.setReceiver(receiver);
        notification.setType(notificationType.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.create(notification);
    }

    @Override
    public List<CommentDTO> listByQuestionId(Long id,Integer type) {
        List<Comment> commentList=commentMapper.listByQuestionId(id,type);
        if(commentList.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人id
        Set<Long> commentators= commentList.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转化成map
        List<User> users=new ArrayList<>();
        for (Long userId : userIds) {
            User user=userMapper.findById(userId);
            users.add(user);
        }
        Map<Long,User> userMap=users.stream().collect(Collectors.toMap(user ->user.getId(), user ->user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOList=commentList.stream().map(comment -> {
           CommentDTO commentDTO=new CommentDTO();
           BeanUtils.copyProperties(comment,commentDTO);
           commentDTO.setUser(userMap.get(comment.getCommentator()));
           return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOList;
    }

}
