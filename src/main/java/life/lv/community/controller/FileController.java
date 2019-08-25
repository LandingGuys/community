package life.lv.community.controller;

import life.lv.community.VO.FileVO;
import life.lv.community.mapper.UserMapper;
import life.lv.community.model.User;
import life.lv.community.provider.AliYunProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@Slf4j
public class FileController {
    @Autowired
    private AliYunProvider aliYunProvider;
    @Autowired
    private UserMapper userMapper;
    @ResponseBody()
    @RequestMapping("/file/upload")
    public FileVO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            String fileName= aliYunProvider.upload(file.getInputStream(),file.getOriginalFilename());
            FileVO fileVO = new FileVO();
            fileVO.setSuccess(1);
            fileVO.setUrl(fileName);
            return  fileVO;
        } catch (Exception e) {
            log.error("upload error", e);
            e.printStackTrace();
            FileVO fileVO = new FileVO();
            fileVO.setSuccess(0);
            fileVO.setMessage("上传失败");
            return fileVO;
        }
    }
    @RequestMapping("/file/imageUpload")
    public String updateImage(@RequestParam("id") long id, @RequestParam("uploadpic") MultipartFile file) {
        try {
            String fileName= aliYunProvider.upload(file.getInputStream(),file.getOriginalFilename());
            User user=userMapper.findById(id);
            user.setAvatarUrl(fileName);
            userMapper.update(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/profile/personal";
    }


}
