package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
  @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
      /*  log.info(file.toString());*/

        String originalFilename = file.getOriginalFilename();
       String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
          String fileName =  UUID.randomUUID().toString()+suffix;
//先判断是否存在目录 若不存在 则创建目录
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }


    /**
     * 图片文件回显回去（在浏览器中显示出来）
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name)); //输入流 将硬盘中的
            //内容读取到内存当中
             ServletOutputStream outputStream = response.getOutputStream();
             response.setContentType("image/jpeg");
             int len = 0 ;
             byte[] bytes = new byte[1024];
             while ((len=fileInputStream.read(bytes))!=-1){
                 outputStream.write(bytes,0,len);
                 outputStream.flush();
             }
         outputStream.close();
             fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
