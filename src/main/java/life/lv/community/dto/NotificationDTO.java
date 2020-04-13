package life.lv.community.dto;

import life.lv.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private String outerContent;
    private Long outerId;
    private String typeName;
    private Integer type;
    private User user;



}
