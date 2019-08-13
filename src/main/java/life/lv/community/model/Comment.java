package life.lv.community.model;

import lombok.Data;

@Data
public class Comment {
    //主键 评论id
    private Long id;
    //父类id
    private Long  parentId;
    //父类类型
    private Integer type;
    //评论人id
    private Long commentator;
    //评论内容
    private String content;
    //创建时间
    private Long gmtCreate;
    //更新时间
    private Long gmtModified;
    //点赞数
    private Long likeCount;
    //评论数
    private Long commentCount;
}
