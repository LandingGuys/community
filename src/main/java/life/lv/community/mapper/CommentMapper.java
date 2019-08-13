package life.lv.community.mapper;

import life.lv.community.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment(parent_id,type,commentator,content,gmt_create,gmt_modified) values(#{parentId},#{type},#{commentator},#{content},#{gmtCreate},#{gmtModified})")
    void insert(Comment comment);
    @Select("select * from comment where id=#{parentId}")
    Comment selectParentId(@Param("parentId") Long parentId);
    @Select("select * from comment where parent_id=#{parentId} and type=#{type} order by gmt_create desc")
    List<Comment> listByQuestionId(@Param("parentId") Long parentId,@Param("type") Integer type);
    @Update("update comment set comment_count=comment_count+1 where id=#{id}")
    void updateComment(Comment comment);
}
