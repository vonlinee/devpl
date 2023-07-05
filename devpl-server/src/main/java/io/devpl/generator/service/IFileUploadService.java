package io.devpl.generator.service;

import io.devpl.generator.domain.param.FileUploadParam;
import io.devpl.generator.domain.param.MultiFileUploadParam;
import io.devpl.generator.domain.param.SingleFileUploadParam;
import io.devpl.generator.domain.vo.FileUploadResult;

/**
 * 文件上传服务
 * <a href="https://juejin.cn/post/6980142557066067982">...</a>
 */
public interface IFileUploadService {

    /**
     * 分配存储路径
     * @param param 文件上传参数
     * @return 存储路径
     */
    String assignFilePath(FileUploadParam param);

    /**
     * 上传单个文件
     * @param param 单文件上传参数
     * @return 上传文件路径
     */
    FileUploadResult uploadSingleFile(SingleFileUploadParam param);

    /**
     * 多文件上传
     * @param param 多文件上传参数
     */
    FileUploadResult uploadMultiFiles(MultiFileUploadParam param);
}
