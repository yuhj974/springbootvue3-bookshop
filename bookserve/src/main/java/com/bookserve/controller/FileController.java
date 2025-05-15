package com.bookserve.controller;

import com.bookserve.utils.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        File directory = new File(filePath);

        // 检查目录是否存在，不存在则创建（包括父目录）
        if (!directory.exists()) {
            directory.mkdirs();  // 使用 mkdirs() 创建多级目录
        }

        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + fileName;
        try {
            // 使用 Files.write 写入文件内容
            Files.write(Paths.get(realPath), file.getBytes());
            String url = "http://localhost:8080/files/download/" + fileName;
            return Result.success(url);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败");
        }
    }

    /**
     * 文件下载
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        try {
            String realPath = filePath + fileName;
            File file = new File(realPath);

            if (!file.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件不存在");
                return;
            }

            response.addHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");

            // 使用 Files.readAllBytes 读取文件内容
            OutputStream os = response.getOutputStream();
            os.write(Files.readAllBytes(file.toPath()));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}