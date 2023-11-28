package io.devpl.backend.service;

import io.devpl.backend.domain.param.FileUploadParam;
import io.devpl.backend.domain.param.MultiFileUploadParam;
import io.devpl.backend.domain.param.SingleFileUploadParam;
import io.devpl.backend.domain.vo.FileUploadVO;

/**
 * 文件上传服务
 * <a href="https://juejin.cn/post/6980142557066067982">...</a>
 */
public interface FileUploadService {

    /**
     * 获取相对路径
     * @param param 文件上传参数
     * @return 相对路径
     */
    String getRelativePath(FileUploadParam param);

    /**
     * 分配存储路径
     * @param pathSegments 文件路径片段
     * @return 存储路径
     */
    String assignFilePath(String... pathSegments);

    /**
     * 上传单个文件
     * @param param 单文件上传参数
     * @return 上传文件路径
     */
    FileUploadVO uploadSingleFile(SingleFileUploadParam param);

    /**
     * 多文件上传
     * @param param 多文件上传参数
     */
    FileUploadVO uploadMultiFiles(MultiFileUploadParam param);
}
