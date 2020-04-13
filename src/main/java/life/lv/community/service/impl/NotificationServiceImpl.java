package life.lv.community.service.impl;

import life.lv.community.dto.NotificationDTO;
import life.lv.community.dto.PageinationDTO;
import life.lv.community.enums.NotificationStatusEnum;
import life.lv.community.enums.NotificationTypeEnum;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.CommentMapper;
import life.lv.community.mapper.NotificationMapper;
import life.lv.community.mapper.QuestionMapper;
import life.lv.community.mapper.UserMapper;
import life.lv.community.model.*;
import life.lv.community.service.NotificationService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    //查询通知列表

    @Override
    public PageinationDTO listUser(Long userId, Integer page, Integer pageNum,String action) {
        PageinationDTO pageinationDTO = new PageinationDTO();
        NotificationExample notificationExample = new NotificationExample();
        if("replies".equals(action)){
            notificationExample.createCriteria().andReceiverEqualTo(userId);
        }else {
            notificationExample.createCriteria().andNotifierEqualTo(userId);
        }
        Integer totalUserCount= (int)notificationMapper.countByExample(notificationExample);

        Integer totalPage = totalUserCount % pageNum == 0 ? totalUserCount / pageNum : totalUserCount / pageNum + 1;
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage && totalPage != 0) {
            page = totalPage;
        }
        pageinationDTO.setPageination(page, totalPage);


        Integer offset=pageNum*(page-1);
        NotificationExample example = new NotificationExample();
        //通过action 查询是通知还是个人动态 dynamic 个人动态 reolies 通知
        if("replies".equals(action)){
            example.createCriteria().andReceiverEqualTo(userId);
            example.setOrderByClause("gmt_create desc");
        }else {
            example.createCriteria().andNotifierEqualTo(userId);
            example.setOrderByClause("gmt_create desc");
        }
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, pageNum));
        User user = userMapper.selectByPrimaryKey(userId);
        List<NotificationDTO> notificationDTOList=new ArrayList<>();

        if (notificationList.size() == 0) {
            return pageinationDTO;
        }

        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTO.setUser(user);
            if("dynamic".equals(action)){
                //通知的类型 如果是评论 显示评论内容 如果是点赞关注 显示问题内容
                if(notification.getType().equals(NotificationTypeEnum.REPLY_COMMENT.getType())||notification.getType().equals(NotificationTypeEnum.REPLY_QUESTION.getType())){
                    notificationDTO.setOuterContent(notification.getOuterTitle());
                    Question question = questionMapper.selectByPrimaryKey(notification.getOuterId());
                    notificationDTO.setOuterTitle(question.getTitle());
                }else if(NotificationTypeEnum.LIKE_COMMENT.getType().equals(notification.getType())){
                    Comment comment = commentMapper.selectByPrimaryKey(notification.getOuterId());
                    notificationDTO.setOuterContent(comment.getContent());
                }
                else{
                    Question question = questionMapper.selectByPrimaryKey(notification.getOuterId());
                    notificationDTO.setOuterContent(question.getTitle());
                }
            }

            notificationDTOList.add(notificationDTO);
        }

        pageinationDTO.setData(notificationDTOList);
        pageinationDTO.setTotalCount(totalUserCount);

        return pageinationDTO;
    }


    //记录通知数
    @Override
    public Long unReadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        long unReadCount = notificationMapper.countByExample(example);
        //Long unReadCount=notificationMapper.unReadCount(userId, NotificationStatusEnum.UNREAD.getStatus());
        return unReadCount;
    }

    //阅读通知

    @Override
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //Notification notification = notificationMapper.selectById(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        //notificationMapper.updateStatus(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
    @Override
    public void readAll(User user) {
        Notification notification=new Notification();
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(user.getId());
        notificationMapper.updateByExampleSelective(notification, example);
        //notificationMapper.updateByExample(notification, example);

    }

    @Override
    public void deleteRead(User user) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(user.getId())
                .andStatusEqualTo(NotificationStatusEnum.READ.getStatus());
        notificationMapper.deleteByExample(example);
    }

    @Override
    public Notification findById(Long id) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        return notification;
    }
}
