package io.devpl.generator.service;

import io.devpl.generator.common.FileStorageStrategy;
import io.devpl.generator.domain.param.FileUploadParam;
import io.devpl.generator.domain.param.MultiFileUploadParam;
import io.devpl.generator.domain.param.SingleFileUploadParam;
import io.devpl.generator.domain.vo.FileUploadResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件上传服务实现类
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements IFileUploadService {

    @Resource
    FileStorageStrategy fileStorageStrategy;

    @Value(value = "${devpl.file.upload.root}")
    private String uploadRootDir;

    @Override
    public String assignFilePath(FileUploadParam param) {
        return fileStorageStrategy.concat(uploadRootDir, param.getFolder());
    }

    @Override
    public FileUploadResult uploadSingleFile(SingleFileUploadParam param) {
        String path = fileStorageStrategy.concat(assignFilePath(param), param.getFilename());
        try (InputStream fileInputStream = param.getFile().getInputStream()) {
            fileStorageStrategy.write(path, fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileUploadResult result = new FileUploadResult();
        result.addFile(path);
        return result;
    }

    @Override
    public FileUploadResult uploadMultiFiles(MultiFileUploadParam param) {
        FileUploadResult result = new FileUploadResult();
        MultipartFile[] files = param.getFiles();
        return result;
    }
}
