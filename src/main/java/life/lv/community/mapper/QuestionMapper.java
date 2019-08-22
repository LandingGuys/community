package life.lv.community.mapper;

import life.lv.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);
    @Select("select * from question order by gmt_create desc limit #{offset},#{pageNum}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param("pageNum") Integer pageNum);
    @Select("select count(1) from question")
    Integer count();
    @Select("select count(1) from question where creator=${userId};")
    Integer countUser(@Param(value = "userId") Long userId);
    @Select("select * from question where creator=#{userId} order by gmt_create desc limit #{offset},#{pageNum}")
    List<Question> listUser(@Param(value = "userId") Long userId, @Param(value = "offset") Integer offset, Integer pageNum);

    @Select("select * from question where id=#{id}")
    Question getById(@Param("id") Long id);
    @Update("update question set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    Integer update(Question question);
    @Update("update question set view_count=view_count+1 where id=#{id}")
    void updateView(Question question);
    @Update("update question set comment_count=comment_count+1 where id=#{id}")
    void updateComment(Question question);
    @Select("select * from question where tag REGEXP #{tag} and id!=#{id}")
    List<Question> questionByTagList(Question question);
    @Select("select * from question where title REGEXP #{search} order by gmt_create desc limit #{offset},#{pageNum}")
    List<Question> questionBySearchList(@Param("search") String search,@Param(value = "offset") Integer offset, @Param("pageNum") Integer pageNum);
    @Select("select count(*) from question where title REGEXP #{search}")
    Integer countBySearch(@Param(value = "search") String search);
    @Update("update question set like_count=like_count+1 where id=#{id}")
    void updateLike(Question question);
}
