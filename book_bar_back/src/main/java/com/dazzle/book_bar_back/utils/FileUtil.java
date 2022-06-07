package com.dazzle.book_bar_back.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName:FileUtil
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/22 下午 4:10
 * Version V1.0
 */
@Component
@Slf4j
public class FileUtil {
    public final static String AVATAR_PATH = "static/uploads/image/employee/avatars/";
    public String uploadFile(MultipartFile file, String basePath) throws Exception
    {
        if (file.isEmpty()) {
            throw new Exception();
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = StringUtil.allocateUuid() + suffix;//服务端文件
        try {
            //Linux环境需修改
            File path = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            //File path = new File("./");
            if (!path.exists()) {
                path = new File("");
            }
            File upload = new File(path.getAbsolutePath(), basePath);
            if (!upload.exists()) {
                upload.mkdirs();
            }
            String uploadPath = upload + "\\";

            file.transferTo(new File(uploadPath + fileName));
            return basePath + fileName;
        }catch (Exception e)
        {
            throw new Exception();
        }
    }
}

