package life.lv.community.model;

import lombok.Data;

@Data
public class User {
    //主键 用户id
    private Long id;
    //github用户id
    private String accountId;
    //github用户姓名
    private String name;
    //cookie 密钥
    private String token;
    //创建时间
    private Long gmtCreate;
    //更新时间
    private Long gmtModified;
    //github用户头像
    private String avatarUrl;



}
