package life.lv.community.service.impl;

import life.lv.community.dto.PageinationDTO;
import life.lv.community.dto.QuestionDTO;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.QuestionMapper;
import life.lv.community.mapper.UserMapper;
import life.lv.community.model.Question;
import life.lv.community.model.User;
import life.lv.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public QuestionDTO getById(Long id) {
        Question question=questionMapper.getById(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user=userMapper.findById(question.getCreator());
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }
    //用户发布的问题
    @Override
    public PageinationDTO listUser(Long userId, Integer page, Integer pageNum) {
        PageinationDTO pageinationDTO = new PageinationDTO();
        Integer totalUserCount=questionMapper.countUser(userId);
        //计算总页数
        Integer offset = getInteger(page, pageNum, pageinationDTO, totalUserCount);
        List<Question> questionList=questionMapper.listUser(userId,offset,pageNum);
        forQuestionDTO(pageinationDTO, questionList);
        return pageinationDTO;
    }
    //所有问题(加search)
    @Override
    public PageinationDTO list(String search,Integer page, Integer pageNum) {
        PageinationDTO pageinationDTO = new PageinationDTO();
        if(StringUtils.isNotBlank(search)){
            String[] tags=StringUtils.split(search," ");
            search= Arrays.stream(tags).collect(Collectors.joining("|"));
            Integer totalCount=questionMapper.countBySearch(search);
            //计算总页数
            Integer offset = getInteger(page, pageNum, pageinationDTO, totalCount);
            List<Question> questionList=questionMapper.questionBySearchList(search,offset,pageNum);
            forQuestionDTO(pageinationDTO, questionList);
            return pageinationDTO;
        }else{
            Integer totalCount=questionMapper.count();
            //计算总页数
            Integer offset = getInteger(page, pageNum, pageinationDTO, totalCount);
            List<Question> questionList=questionMapper.list(offset,pageNum);
            forQuestionDTO(pageinationDTO, questionList);
            return pageinationDTO;
        }
    }

    private void forQuestionDTO(PageinationDTO pageinationDTO, List<Question> questionList) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageinationDTO.setData(questionDTOList);
    }
    private Integer getInteger(Integer page, Integer pageNum, PageinationDTO pageinationDTO, Integer totalCount) {
        Integer totalPage = totalCount % pageNum == 0 ? totalCount / pageNum : totalCount / pageNum + 1;
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage && totalPage != 0) {
            page = totalPage;
        }
        pageinationDTO.setPageination(page, totalPage);
        return (pageNum * (page - 1));
    }

    @Override
    public void createOrUpdate(Question question) {
        Question dbQuestion=questionMapper.getById(question.getId());
        if(dbQuestion==null){
            //插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            //更新
            dbQuestion.setGmtModified(System.currentTimeMillis());
            dbQuestion.setTitle(question.getTitle());
            dbQuestion.setDescription(question.getDescription());
            dbQuestion.setTag(question.getTag());
            Integer update=questionMapper.update(dbQuestion);
            if(update!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }

    }
    @Override
    public void incView(Long id) {
        Question question = questionMapper.getById(id);
        questionMapper.updateView(question);
    }

    @Override
    public void incLike(long id) {
        Question question = questionMapper.getById(id);
        questionMapper.updateLike(question);
    }

    @Override
    public List<QuestionDTO> questionByTagList(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String regexTag=StringUtils.replace(questionDTO.getTag(),",","|");
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexTag);
        List<Question> questionList=questionMapper.questionByTagList(question);
        List<QuestionDTO> questionDTOS=questionList.stream().map(q -> {
            QuestionDTO questions = new QuestionDTO();
            BeanUtils.copyProperties(q, questions);
            return questions;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
