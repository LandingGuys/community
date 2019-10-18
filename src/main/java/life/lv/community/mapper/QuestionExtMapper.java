package life.lv.community.mapper;

import life.lv.community.dto.QuestionQueryDTO;
import life.lv.community.model.Question;
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

    @Select("select * from question where tag REGEXP #{tag} and id!=#{id}")
    List<Question> questionByTagList(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
