package cn.kanyun.qurtzjdbc.controller;


import cn.kanyun.qurtzjdbc.entity.Constant;
import cn.kanyun.qurtzjdbc.entity.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Kanyun
 * @date 2019/6/21
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @GetMapping("/downloadTemplateFile")
    public void downloadTemplateFile(HttpServletResponse response) {

    }


    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@Param("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.errorHandler("上传失败，请选择文件");
        }
        String fileName = file.getOriginalFilename();
        File dest = new File(Constant.uploadFilePath + fileName);
        try {
            file.transferTo(dest);
            return Result.successHandler();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.errorHandler(e.getMessage());
        }
    }
}
