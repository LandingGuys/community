package life.lv.community.dto;
/**
 *获取github授权连接对象
 */

import lombok.Data;

@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
