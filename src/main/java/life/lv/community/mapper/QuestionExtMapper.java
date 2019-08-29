package life.lv.community.mapper;

import life.lv.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface QuestionExtMapper {

    @Update("update question set view_count=view_count+1 where id=#{id}")
    void incQuestionView(Question question);

    @Update("update question set comment_count=comment_count+1 where id=#{id}")
    void incCountComment(Question question);

    @Update("update question set like_count=like_count+1 where id=#{id}")
    void incQuestionLike(Question question);

    @Select("select count(*) from question where title REGEXP #{search}")
    Integer countBySearch(@Param(value = "search") String search);

    @Select("select * from question where title REGEXP #{search} order by gmt_create desc limit #{offset},#{pageNum}")
    List<Question> questionBySearchList(@Param("search") String search, @Param(value = "offset") Integer offset, @Param("pageNum") Integer pageNum);

    @Select("select * from question where tag REGEXP #{tag} and id!=#{id}")
    List<Question> questionByTagList(Question question);
}
