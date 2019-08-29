package life.lv.community.service;

import life.lv.community.dto.PageinationDTO;
import life.lv.community.dto.QuestionDTO;
import life.lv.community.model.QuestionWithBLOBs;

import java.util.List;

public interface QuestionService {
    public PageinationDTO list(String search,Integer page, Integer pageNum);

    public PageinationDTO listUser(Long id, Integer page, Integer pageNum);

    public QuestionDTO getById(Long id);

    public void createOrUpdate(QuestionWithBLOBs question);

    public void incView(Long id);

    public List<QuestionDTO> questionByTagList(QuestionDTO questionDTO);

    String incLike(long id,long userId);
}
