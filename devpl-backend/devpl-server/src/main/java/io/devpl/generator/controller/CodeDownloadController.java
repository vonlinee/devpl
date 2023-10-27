package io.devpl.generator.controller;

import io.devpl.generator.utils.ServletUtils;
import io.devpl.generator.service.CodeGenService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成控制器
 */
@Controller
@RequestMapping("/gen/generator")
public class CodeDownloadController {

    @Resource
    CodeGenService codeGenService;

    /**
     * 生成代码（zip压缩包）
     */
    @GetMapping("/download")
    public void download(String tableIds, HttpServletResponse response) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            // 生成代码
            for (String tableId : tableIds.split(",")) {
                codeGenService.downloadCode(Long.parseLong(tableId), zip);
            }
            // zip压缩包数据
            ServletUtils.downloadFile(response, "devpl.zip", outputStream);
        }
    }
}
