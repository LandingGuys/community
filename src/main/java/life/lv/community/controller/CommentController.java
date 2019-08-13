package life.lv.community.controller;

import life.lv.community.VO.ResultVO;
import life.lv.community.dto.CommentDTO;
import life.lv.community.dto.CommetCreateDTO;
import life.lv.community.enums.CommentTypeEnum;
import life.lv.community.exception.CustomizeErrorCode;
import life.lv.community.model.Comment;
import life.lv.community.model.User;
import life.lv.community.service.CommentService;
import life.lv.community.service.QuestionService;
import life.lv.community.utils.ResultVoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private QuestionService questionService;

    @PostMapping("/comment")
    public Object post(@RequestBody CommetCreateDTO commetCreateDTO,
                       HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if(user == null){
            return ResultVoUtil.error(CustomizeErrorCode.NO_LOGIN.getCode(),CustomizeErrorCode.NO_LOGIN.getMessage());
        }
        if(commetCreateDTO==null|| StringUtils.isBlank(commetCreateDTO.getContent())){
            return ResultVoUtil.error(CustomizeErrorCode.CONTENT_IS_EMPTY.getCode(),CustomizeErrorCode.CONTENT_IS_EMPTY.getMessage());
        }
        Comment comment = new Comment();
        comment.setParentId(commetCreateDTO.getParentId());
        comment.setContent(commetCreateDTO.getContent());
        comment.setType(commetCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        commentService.insert(comment,user);

        return ResultVoUtil.success();
    }


    @GetMapping("/comment/{id}")
    public ResultVO<List> comment(@PathVariable("id") Long id){
        List<CommentDTO> commentDTOList=commentService.listByQuestionId(id, CommentTypeEnum.COMMENT.getType());
        return ResultVoUtil.success(commentDTOList);
    }



}
