package life.lv.community.service.impl;

import life.lv.community.dto.CommentDTO;
import life.lv.community.enums.CommentTypeEnum;
import life.lv.community.enums.NotificationStatusEnum;
import life.lv.community.enums.NotificationTypeEnum;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.*;
import life.lv.community.model.*;
import life.lv.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
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
    private CommentExtMapper commentExtMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

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
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //2.查询有没有该问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //3.插入回复的新评论
            commentMapper.insertSelective(comment);
            //4.增加之前评论的评论数
            commentExtMapper.incCountComment(dbComment.getId());
            //5.通知回复了评论
            createNotify(comment, dbComment.getCommentor(), commentator.getName(), comment.getContent(), NotificationTypeEnum.REPLY_COMMENT, question.getId());

        }else {
            //回复问题
            //1.查询有没有该问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //2.插入回复问题的新评论
            commentMapper.insertSelective(comment);
            //3.增加问题的评论数
            questionExtMapper.incCountComment(question);
            //4.通知回复了问题
            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        //如果评论人是发布人则不通知
        if (receiver .equals(comment.getCommentor()) ) {
            return;
        }
        Notification notification = new Notification();
        notification.setNotifier(comment.getCommentor());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifierName(notifierName);
        notification.setOuterId(outerId);
        notification.setOuterTitle(outerTitle);
        notification.setReceiver(receiver);
        notification.setType(notificationType.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insertSelective(notification);
    }

    @Override
    public List<CommentDTO> listByQuestionId(Long id,Integer type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type);
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        if(commentList.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人id
        Set<Long> commentators= commentList.stream().map(comment -> comment.getCommentor()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转化成map
        List<User> users=new ArrayList<>();
        for (Long userId : userIds) {
            User user = userMapper.selectByPrimaryKey(userId);
            users.add(user);
        }
        Map<Long,User> userMap=users.stream().collect(Collectors.toMap(user ->user.getId(), user ->user));
        //转换comment为commentDTO
        List<CommentDTO> commentDTOList=commentList.stream().map(comment -> {
           CommentDTO commentDTO=new CommentDTO();
           BeanUtils.copyProperties(comment,commentDTO);
           commentDTO.setUser(userMap.get(comment.getCommentor()));
           return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOList;
    }

    @Override
    public String incLike(long id, Long userId) {
        Comment dbComment = commentMapper.selectByPrimaryKey(id);
        if( StringUtils.isBlank(dbComment.getLikeUsed()) || !dbComment.getLikeUsed().contains(userId+",")){
            Comment comment = new Comment();
            comment.setLikeUsed(userId+",");
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andIdEqualTo(dbComment.getId());
            commentMapper.updateByExampleSelective(comment, commentExample);
            commentExtMapper.incLikeComment(id);
            return "success";
        }else{
            return "error";
        }


    }
}
