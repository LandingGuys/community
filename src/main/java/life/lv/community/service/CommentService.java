package life.lv.community.service;

import life.lv.community.dto.CommentDTO;
import life.lv.community.model.Comment;
import life.lv.community.model.User;

import java.util.List;

public interface CommentService {

    void insert(Comment comment, User commentator);

    List<CommentDTO> listByQuestionId(Long id,Integer type);
}
