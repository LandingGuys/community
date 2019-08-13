package life.lv.community.enums;

import lombok.Getter;

@Getter
public enum NotificationStatusEnum {

    UNREAD(0), READ(1);
    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
