package life.lv.community.model;

import lombok.Data;

@Data
public class Question {
    //主键 问题id
    private Long id;
    //问题 题目
    private String title;
    //问题 描述（内容）
    private String description;
    //问题 标签
    private String tag;
    //创建时间
    private Long gmtCreate;
    //更新时间
    private Long gmtModified;
    //问题 提出者id
    private Long creator;
    //浏览次数
    private Integer viewCount;
    //点赞数
    private Integer likeCount;
    //评论数
    private Integer commentCount;
}
