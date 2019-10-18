package life.lv.community.dto;

import lombok.Data;

/**
 * @Author: F
 * @Date: 2019/10/10 16:44
 */
@Data
public class QQAccessTokenDTO {
    private String grant_type;
    private String client_id;
    private String code;
    private String client_secret;
    private String redirect_uri;
}
