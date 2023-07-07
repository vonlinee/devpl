package io.devpl.generator.service;

import io.devpl.generator.common.FileStorageStrategy;
import io.devpl.generator.domain.param.FileUploadParam;
import io.devpl.generator.domain.param.MultiFileUploadParam;
import io.devpl.generator.domain.param.SingleFileUploadParam;
import io.devpl.generator.domain.vo.FileUploadVO;
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

    /**
     * 上传文件存放根目录
     */
    @Value(value = "${devpl.file.upload.root}")
    private String uploadRootDir;

    @Override
    public String getRelativePath(FileUploadParam param) {
        return param.getFolder();
    }

    @Override
    public String assignFilePath(String... pathSegments) {
        return fileStorageStrategy.concat(pathSegments);
    }

    /**
     * 上传，返回文件相对路径
     * @param param 单文件上传参数
     * @return 上传结果
     */
    @Override
    public FileUploadVO uploadSingleFile(SingleFileUploadParam param) {
        String path = fileStorageStrategy.concat(assignFilePath(param.getFolder(), param.getFilename()));
        try (InputStream fileInputStream = param.getFile().getInputStream()) {
            fileStorageStrategy.write(uploadRootDir + "/" + path, fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileUploadVO result = new FileUploadVO();
        result.addFilePath(path);
        return result;
    }

    @Override
    public FileUploadVO uploadMultiFiles(MultiFileUploadParam param) {
        FileUploadVO result = new FileUploadVO();
        MultipartFile[] files = param.getFiles();
        return result;
    }
}
