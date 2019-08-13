package life.lv.community.service;

import life.lv.community.dto.NotificationDTO;
import life.lv.community.dto.PageinationDTO;
import life.lv.community.model.User;

public interface NotificationService {

    PageinationDTO listUser(Long userId, Integer page, Integer pageNum);

    Long unReadCount(Long userId);

    NotificationDTO read(Long id, User user);
}
