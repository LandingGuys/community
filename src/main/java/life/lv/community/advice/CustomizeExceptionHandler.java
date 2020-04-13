package life.lv.community.advice;


import com.alibaba.fastjson.JSON;
        import life.lv.community.VO.ResultVO;
        import life.lv.community.exception.CustomizeErrorCode;
        import life.lv.community.exception.CustomizeException;
        import life.lv.community.utils.ResultVoUtil;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.ControllerAdvice;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.servlet.ModelAndView;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.IOException;
        import java.io.PrintWriter;

@ControllerAdvice
@Slf4j
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model,
                        HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            //返回JSON
            ResultVO resultVO;
            if (e instanceof CustomizeException){
                resultVO=ResultVoUtil.error((CustomizeException) e);
            }else{
                resultVO=ResultVoUtil.error(CustomizeErrorCode.SYS_ERROR.getCode(),CustomizeErrorCode.SYS_ERROR.getMessage());
            }

            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultVO));

                writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return null;
        } else {
            // 错误页面跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                log.error("handle error",e);
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }


}
