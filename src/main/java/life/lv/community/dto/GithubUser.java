package life.lv.community.dto;
/**
 *
 * Github用户信息
 */

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    @SerializedName("avatar_url")
    private String avatarUrl;
}
