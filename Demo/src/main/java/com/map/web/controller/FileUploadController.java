package com.map.web.controller;



import com.map.utils.QiniuCloudUtil;
import com.map.web.model.ResultBuilder;
import com.map.web.model.ResultModel;
import com.map.web.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;



@RestController
public class FileUploadController {

    Logger logger = Logger.getLogger(FileUploadController.class);


    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value="/qiniuupload/{pointId:\\d+}", method=RequestMethod.POST)
    public ResultModel uploadImg(@PathVariable int pointId, String title,
            @RequestParam MultipartFile image, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");
        String url = null;
        if (image.isEmpty()) {
            return ResultBuilder.getFailure(1,"文件内容为空");
        }

        try {
            byte[] bytes = image.getBytes();
            String imageName = UUID.randomUUID().toString();

            QiniuCloudUtil qiniuUtil = new QiniuCloudUtil();
            try {
                //使用base64方式上传到七牛云
                url = qiniuUtil.put64image(bytes, imageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            return ResultBuilder.getFailure(-1,"文件上传七牛云发生异常！");
        }
//        return ResultBuilder.getSuccess(url,"上传成功");
//        ImageMessage imageMessage = new ImageMessage();
//
//        imageMessage.setTitle(title);
//        imageMessage.setUrls(url);
//        return informationService.addMangPhotosMessage(userId, pointId, imageMessage);
        return null;
    }


    private ResultModel isFileNull(MultipartFile file) {
        if (file == null) {
            return ResultBuilder.getFailure(1, "文件内容为空");
        }
        if (file.isEmpty()) {
            return ResultBuilder.getFailure(1, "文件内容为空");
        }

        return null;
    }


}