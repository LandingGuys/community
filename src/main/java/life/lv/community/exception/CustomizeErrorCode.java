package life.lv.community.exception;

import lombok.Getter;

@Getter
public enum CustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "你找到问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "兄dei先去登录吧！"),
    SYS_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不要换个试试？"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL(2010, "图片上传失败"),
    INVALID_INPUT(2011, "非法输入"),
    INVALID_OPERATION(2012, "兄弟，是不是走错房间了？"),
    EDIT_QUESTION_FAIL(2013,"兄dei你虽然登录了但也不能改别人的问题呀！！！"),
    EDIT_QUEDTION_NOTLOGIN(2014,"兄dei你还没登录就想改别人的问题？？？"),
    TITLE_IS_EMPTY(2015,"问题标题不能为空~"),
    DESCRIPTION_IS_EMPTY(2016,"问题内容不能为空~"),
    TAG_IS_EMPTY(2017,"问题标签不能为空~"),
    TAG_IS_WRONGFUL(2018,"标签不合法"),
    CANT_LIKE_YOURSELF_QUESTION(2019,"兄dei不可以给自己点赞哦！" ),
    REPEAT_LIKE(2020,"兄dei你已经点过了！"),
    ;
    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
