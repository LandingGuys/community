package life.lv.community.service;

import life.lv.community.dto.NotificationDTO;
import life.lv.community.dto.PageinationDTO;
import life.lv.community.model.Notification;
import life.lv.community.model.User;



public interface NotificationService {

    PageinationDTO listUser(Long userId, Integer page, Integer pageNum,String action);

    Long unReadCount(Long userId);

    NotificationDTO read(Long id, User user);

    void readAll(User user);

    void deleteRead(User user);

    Notification findById(Long id);
}
