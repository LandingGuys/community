package life.lv.community.service.impl;

import life.lv.community.dto.PageinationDTO;
import life.lv.community.dto.QuestionDTO;
import life.lv.community.dto.QuestionQueryDTO;
import life.lv.community.enums.SortEnum;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.exception.CustomizeException;
import life.lv.community.mapper.QuestionExtMapper;
import life.lv.community.mapper.QuestionMapper;
import life.lv.community.mapper.UserMapper;
import life.lv.community.model.Question;
import life.lv.community.model.QuestionExample;
import life.lv.community.model.QuestionWithBLOBs;
import life.lv.community.model.User;
import life.lv.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public QuestionDTO getById(Long id) {
        Question question=questionMapper.selectByPrimaryKey(id);
        //Question question=questionMapper.getById(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        //User user=userMapper.findById(question.getCreator());
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }
    //用户发布的问题
    @Override
    public PageinationDTO listUser(Long userId, Integer page, Integer pageNum) {
        PageinationDTO pageinationDTO = new PageinationDTO();
        //计算总页数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalUserCount= (int)questionMapper.countByExample(questionExample);

        Integer offset = getInteger(page, pageNum, pageinationDTO, totalUserCount);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, pageNum));
        //List<Question> questionList=questionMapper.listUser(userId,offset,pageNum);
        forQuestionDTO(pageinationDTO, questionList);
        return pageinationDTO;
    }
    //所有问题(加search)
    @Override
    public PageinationDTO list(String search,String tag,String sort,Integer page, Integer pageNum) {
        PageinationDTO pageinationDTO = new PageinationDTO();
        if(StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays.stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }
        Integer totalPage;
        QuestionQueryDTO questionQueryDTO=new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        if(StringUtils.isNotBlank(tag)){
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            questionQueryDTO.setTag(tag);
        }

        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase().equals(sort)) {
                questionQueryDTO.setSort(sort);

                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }
        Integer totalCount=questionExtMapper.countBySearch(questionQueryDTO);
            //计算总页数
        Integer offset = getInteger(page, pageNum, pageinationDTO, totalCount);
        questionQueryDTO.setSize(pageNum);
        questionQueryDTO.setPage(offset);
        List<Question> questionList=questionExtMapper.selectBySearch(questionQueryDTO);
        forQuestionDTO(pageinationDTO, questionList);
        return pageinationDTO;
    }

    private void forQuestionDTO(PageinationDTO pageinationDTO, List<Question> questionList) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            //User user = userMapper.findById(question.getCreator());
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
    public void createOrUpdate(QuestionWithBLOBs question) {

        if(question.getId()==null){
            //插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert((QuestionWithBLOBs)question);
            //questionMapper.insertSelective(question);
           // questionMapper.create(question);
        }else{
            //更新
            Question dbQuestion = questionMapper.selectByPrimaryKey(question.getId());
            if(dbQuestion==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            if(dbQuestion.getCreator().longValue()!=question.getCreator().longValue()){
                throw new CustomizeException(CustomizeErrorCode.INVALID_OPERATION);
            }
            QuestionWithBLOBs updateQuestion = new QuestionWithBLOBs();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setId(question.getId());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(dbQuestion.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            //int update = questionMapper.updateByExample(updateQuestion, questionExample);
            //Integer update=questionMapper.update(dbQuestion);
            if(update!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }

    }
    @Override
    public void incView(Long id) {
        Question question =questionMapper.selectByPrimaryKey(id);
        questionExtMapper.incQuestionView(question);
    }

    @Override
    public String incLike(long id,long userId) {
        Question dbQuestion =questionMapper.selectByPrimaryKey(id);
        if( StringUtils.isBlank(((QuestionWithBLOBs) dbQuestion).getLikeUsed()) ||!((QuestionWithBLOBs) dbQuestion).getLikeUsed().contains(userId+",")){
            QuestionWithBLOBs question = new QuestionWithBLOBs();
            question.setLikeUsed(userId+",");
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(dbQuestion.getId());
            questionMapper.updateByExampleSelective(question, example);
            questionExtMapper.incQuestionLike(dbQuestion);
            return "success";
        }else{
            return "error";
        }

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
        List<Question> questionList=questionExtMapper.questionByTagList(question);
        List<QuestionDTO> questionDTOS=questionList.stream().map(q -> {
            QuestionDTO questions = new QuestionDTO();
            BeanUtils.copyProperties(q, questions);
            return questions;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
