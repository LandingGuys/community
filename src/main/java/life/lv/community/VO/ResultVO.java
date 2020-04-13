package life.lv.community.VO;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultVO<T> {
    //错误码
    private Integer code;
    //提示信息
    private String message;
    //返回具体内容
    private T data;
    //返回多个具体数据
    private Map<String,Object> extend=new HashMap<>();

    public ResultVO addMsg(String key,Object value){
        this.extend.put(key,value);
        return this;
    }
}
