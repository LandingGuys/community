package life.lv.community.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
@Data
public class QuestionForm {
    @NotEmpty(message = "标题必填")
    private String title;
    @NotEmpty(message = "内容必填")
    private String description;
    @NotEmpty(message = "标签必填")
    private String tag;
}
