package life.lv.community.utils;

import life.lv.community.enums.NotificationStatusEnum;
import life.lv.community.enums.NotificationTypeEnum;
import life.lv.community.model.Comment;
import life.lv.community.model.Notification;
import life.lv.community.model.Question;

public class CreateNotifyUtil {
    public static Notification createNotify(Object object, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId){
        //如果评论人是发布人则不通知
        Notification notification = new Notification();
        if(object instanceof Comment){
            Comment comment=(Comment) object;
            if (receiver == comment.getCommentator()) {
                return null;
            }
            notification.setNotifier(comment.getCommentator());
        }
        if(object instanceof Question){
            Question question=(Question)object;
            if(receiver==question.getCreator()){
                return null;
            }
            notification.setNotifier(question.getCreator());
        }
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifierName(notifierName);
        notification.setOuterid(outerId);
        notification.setOuterTitle(outerTitle);
        notification.setReceiver(receiver);
        notification.setType(notificationType.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());

        return notification;

    }
}
