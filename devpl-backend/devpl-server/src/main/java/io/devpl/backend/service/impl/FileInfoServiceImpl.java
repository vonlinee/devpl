package io.devpl.backend.service.impl;

import io.devpl.backend.common.mvc.MyBatisPlusServiceImpl;
import io.devpl.backend.dao.FileInfoMapper;
import io.devpl.backend.entity.FileConfig;
import io.devpl.backend.entity.FileInfo;
import io.devpl.backend.service.FileInfoService;
import io.devpl.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileInfoServiceImpl extends MyBatisPlusServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

    /**
     * 文件上传
     *
     * @param mpfList   文件信息集
     * @param bizType   业务类型(必传)
     * @param bizId     业务id
     * @param extraPath 额外的路径，首部和结尾不能带斜杠'/'
     * @return 文件信息
     */
    public List<FileInfo> uploadFile(List<MultipartFile> mpfList, String bizType, Long bizId, String extraPath) {
        // 验证数据begin
        // 获取对应业务文件配置信息
        FileConfig fileConf = new FileConfig();
        // 验证文件信息是否符合配置信息
        if (!validateFileInfo(mpfList, fileConf)) {
            // 验证失败
            log.info("fileInfo is error");  // 打印文件配置信息
            return null;
        }
        // 信息验证end
        List<FileInfo> files = new ArrayList<>();
        FileInfo fileInfo;
        String path = fileConf.getPath();  // 文件存储的目录
        // 获取相对路径，由file_conf、额外路径
        String relativePath = fileConf.getResourceRealm() + "/"
                              + (!StringUtils.hasText(extraPath) ? "" : extraPath + "/");

        // 验证服务器存储路径是否存在，若不存在，则新建文件夹
        File serFile = new File(path + relativePath);
        if (!serFile.exists()) {
            boolean result = serFile.mkdirs();
        }

        // 循环上传文件
        for (MultipartFile mpf : mpfList) {
            String originalFileName = mpf.getOriginalFilename(); // 获取源文件名
            if (originalFileName == null) {
                continue;
            }
            // 生成新文件名
            String newFileName = "F" + UUID.randomUUID().toString().replace("-", "").toUpperCase()
                                 + originalFileName.substring(originalFileName.lastIndexOf("."));
            // 组装数据
            fileInfo = new FileInfo();
            fileInfo.setOriginalName(originalFileName);
            fileInfo.setFileSize(String.valueOf(mpf.getSize() / 1024)); // 单位（kb）
            fileInfo.setFileType(mpf.getContentType());     // 文件类型
            fileInfo.setFileName(newFileName);                        // 文件新名字
            fileInfo.setRelativePath(relativePath + newFileName);    // 文件相对路径
            fileInfo.setFilePath(path + relativePath + newFileName); // 文件物理路径
            fileInfo.setStatus(0);
            // 存储文件并记录到数据库
            try {
                FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(fileInfo.getFilePath()));
                save(fileInfo);
            } catch (IOException e) {
                log.error("upload file error!", e);
                return null;
            }
            files.add(fileInfo);
        }
        return files;
    }

    private boolean validateFileInfo(List<MultipartFile> mpfList, FileConfig fileConf) {
        if (mpfList == null || fileConf == null) {
            return false;
        }
        for (MultipartFile mpf : mpfList) {
            // 验证文件大小是否超出配置大小
            if (StringUtils.hasText(fileConf.getFileSizeLimit()) && mpf.getSize() / 1024 > Integer.parseInt(fileConf.getFileSizeLimit())) {
                return false;
            }
            // 验证文件类型是否符合文件配置的要求
            if (StringUtils.hasText(fileConf.getFileTypeLimit()) && !fileConf.getFileTypeLimit()
                .contains(mpf.getContentType())) {
                return false;
            }
        }
        return true;
    }
}
