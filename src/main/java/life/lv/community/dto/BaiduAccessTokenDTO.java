package life.lv.community.dto;

import lombok.Data;

/**
 * @Author: F
 * @Date: 2019/9/24 21:03
 */
@Data
public class BaiduAccessTokenDTO {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String code;
}
