package io.devpl.generator.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.common.query.ListResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BusinessUtils {

    public static <T> ListResult<T> page2List(IPage<T> page) {
        return new ListResult<>(page.getRecords(), page.getTotal());
    }

    public static void writeMultipartFile(MultipartFile file, File dest) {
        writeMultipartFile(file, dest.toPath());
    }

    public static void writeMultipartFile(MultipartFile file, Path dest) {
        try {
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
