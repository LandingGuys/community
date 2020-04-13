package life.lv.community.mapper;

import life.lv.community.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserExtMapper {
    @Select("select * from user ORDER BY gmt_modified desc limit 1,#{code}")
    List<User> findNewUsers(@Param("code") Integer code);
}