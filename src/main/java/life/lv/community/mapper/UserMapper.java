package life.lv.community.mapper;

import life.lv.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified,avatar_url,user_name,user_region,user_industry,user_introduction) values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl},#{userName},#{userRegion},#{userIndustry},#{userIntroduction})")
    void insert(User user);
    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);
    @Select("select * from user where id=#{id}")
    User findById(@Param("id") Long id);
    @Select("select * from user where account_id=#{accountId}")
    User findByAccountId(@Param("accountId")String accountId);
    @Update("update user set name=#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl},user_name=#{userName},user_region=#{userRegion},user_industry=#{userIndustry},user_introduction=#{userIntroduction} where id=#{id}")
    void update(User user);
}
