package life.lv.community.model;

public class Comment {
    private Long id;

    private Long parentId;

    private Integer type;

    private String content;

    private Long gmtCreate;

    private Long gmtModified;

    private Integer likeCount;

    private Integer commentCount;

    private Long commentor;

    private String likeUsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Long getCommentor() {
        return commentor;
    }

    public void setCommentor(Long commentor) {
        this.commentor = commentor;
    }

    public String getLikeUsed() {
        return likeUsed;
    }

    public void setLikeUsed(String likeUsed) {
        this.likeUsed = likeUsed == null ? null : likeUsed.trim();
    }
}