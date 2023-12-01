package io.devpl.backend.controller;

import io.devpl.backend.service.FileGenerationService;
import io.devpl.backend.utils.ServletUtils;
import io.devpl.backend.utils.Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成控制器
 */
@Controller
@RequestMapping("/api/codegen")
public class CodeDownloadController {

    @Resource
    FileGenerationService codeGenService;

    /**
     * 生成代码（zip压缩包）
     */
    @GetMapping("/download")
    public void download(String tableIds, HttpServletResponse response) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            // 生成代码
            for (String tableId : tableIds.split(",")) {

                String root = codeGenService.getAbsolutePath(codeGenService.generateForTable(Long.parseLong(tableId)));

                File file = new File(root);

                Utils.openDirectory(file.getAbsolutePath());

                System.out.println(file);
            }
            // zip压缩包数据
            ServletUtils.downloadFile(response, "devpl.zip", outputStream);
        }
    }
}
