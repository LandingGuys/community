package life.lv.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


public interface CommentExtMapper {
    @Update("update comment set comment_count=comment_count+1 where id=#{id}")
    void incCountComment(@Param("id") long id);


    @Update("update comment set like_count=like_count+1 where id=#{id}")
    void incLikeComment(@Param("id") long id);
}
