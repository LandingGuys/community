package life.lv.community.utils;

import life.lv.community.VO.ResultVO;
import life.lv.community.exception.CustomizeException;

public class ResultVoUtil {
    public static ResultVO success(Object object){
        ResultVO resultVO=new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(200);
        resultVO.setMessage("成功");
        return resultVO;
    }

    public static ResultVO success(){
        return success(null);
    }

    public static ResultVO error(Integer code,String msg){
        ResultVO resultVO=new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(msg);
        return  resultVO;
    }
    public static ResultVO error(CustomizeException e){
        ResultVO resultVO=new ResultVO();
        resultVO.setCode(e.getCode());
        resultVO.setMessage(e.getMessage());
        return  resultVO;
    }


}
