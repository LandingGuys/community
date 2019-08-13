package life.lv.community.controller;

import life.lv.community.VO.FileVO;
import life.lv.community.provider.AliYunProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class FileController {
    @Autowired
    private AliYunProvider aliYunProvider;

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
}
