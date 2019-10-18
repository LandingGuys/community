package life.lv.community.dto;

import lombok.Data;

@Data
public class PublishDTO {
    private Long  id;
    private String title;
    private String description;
    private String tag;
}
