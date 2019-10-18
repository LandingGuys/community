package life.lv.community.service.impl;

import life.lv.community.dto.NotificationDTO;
import life.lv.community.dto.PageinationDTO;
import life.lv.community.enums.NotificationStatusEnum;
import life.lv.community.enums.NotificationTypeEnum;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.NotificationMapper;
import life.lv.community.mapper.UserMapper;
import life.lv.community.model.Notification;
import life.lv.community.model.NotificationExample;
import life.lv.community.model.User;
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
    //查询通知列表

    @Override
    public PageinationDTO listUser(Long userId, Integer page, Integer pageNum) {
        PageinationDTO pageinationDTO = new PageinationDTO();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalUserCount= (int)notificationMapper.countByExample(notificationExample);
        //Integer totalUserCount=notificationMapper.countUser(userId);
        //计算总页数
        Integer totalPage=totalUserCount%pageNum==0?totalUserCount/pageNum:totalUserCount/pageNum+1;
        if(page<1){
            page=1;
        }
        if(page>totalPage && totalPage!=0){
            page=totalPage;
        }
        pageinationDTO.setPageination(page,totalPage);
        Integer offset=pageNum*(page-1);
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, pageNum));
        //List<Notification> notificationList=notificationMapper.listUser(userId,offset,pageNum);
        List<NotificationDTO> notificationDTOList=new ArrayList<>();

        if (notificationList.size() == 0) {
            return pageinationDTO;
        }

        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

        pageinationDTO.setData(notificationDTOList);
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
}
