package cn.controller;

import cn.common.Code;
import cn.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${cat.path}")
    private String fileRealPath;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(MultipartFile file) throws IOException {
        log.info("传递的file:{}", file);

        String originName = file.getOriginalFilename();
        log.info("原始的file:{}", originName);

        String suffix = originName.substring(originName.lastIndexOf("."));
        log.info("file的后缀:{}", suffix);

        //重置文件名
        String newfileName = UUID.randomUUID() + suffix;
        log.info("重置后的file:{}", newfileName);

        //创建文件夹以存放文件
        File createFile = new File(fileRealPath);
        //查看该文件夹是否存在
        if (!createFile.exists()) {
            createFile.mkdirs();
        }
        file.transferTo(new File(fileRealPath + newfileName));

        return ResponseEntity.status(HttpStatus.OK).body(newfileName);
    }
}
