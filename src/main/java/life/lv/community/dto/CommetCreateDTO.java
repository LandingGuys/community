package life.lv.community.dto;

import lombok.Data;

@Data
public class CommetCreateDTO {
    private Long  parentId;
    private Integer type;
    private String content;
}
