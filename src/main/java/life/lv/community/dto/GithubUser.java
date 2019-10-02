package life.lv.community.dto;
/**
 *
 * Github用户信息
 */

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
