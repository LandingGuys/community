package life.lv.community.mapper;

import life.lv.community.model.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Select("select * from notification where id=#{id}")
    Notification selectById(@Param("id") Long id);
    //查询通知（分页）
    @Select("select * from notification where receiver=#{userId} order by gmt_create desc limit #{offset},#{pageNum}")
    List<Notification> listUser(@Param(value = "userId") Long userId, @Param(value = "offset") Integer offset, Integer pageNum);

    //查询用户本人通知个数
    @Select("select count(1) from notification where receiver=${userId}")
    Integer countUser(@Param(value = "userId") Long userId);

    //添加通知
    @Insert("insert into notification(notifier,receiver,outerid,type,gmt_create,status,notifier_name,outer_title) values(#{notifier},#{receiver},#{outerid},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    void create(Notification notification);

    //阅读通知
    @Update("update notification set status=#{status} where id=#{id}")
    void updateStatus(Notification notification);

    //未读通知数
    @Select("select count(1) from notification where receiver=#{userId} and status=#{status}")
    Long unReadCount(@Param(value = "userId") Long userId,@Param(value = "status") Integer status);




}
