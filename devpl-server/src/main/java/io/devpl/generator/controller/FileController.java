package io.devpl.generator.controller;

import io.devpl.generator.common.utils.Result;
import io.devpl.generator.domain.param.FileDownloadParam;
import io.devpl.generator.domain.param.MultiFileUploadParam;
import io.devpl.generator.domain.param.SingleFileUploadParam;
import io.devpl.generator.domain.vo.FileUploadVO;
import io.devpl.generator.service.IFileUploadService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件相关控制器，文件上传，下载等
 * 参考实现
 * <a href="https://blog.csdn.net/minaki_/article/details/85163343">...</a>
 */
@RestController
@RequestMapping(value = "/api/file")
public class FileController {

    @Resource
    IFileUploadService fileUploadService;

    /**
     * 单文件上传
     * @return 返回文件上传成功的路径
     */
    @PostMapping(value = "/upload/single")
    public Result<FileUploadVO> uploadFile(@Validated SingleFileUploadParam param) {
        try {
            return Result.ok(fileUploadService.uploadSingleFile(param));
        } catch (Exception exception) {
            return Result.exception(exception);
        }
    }

    /**
     * 多文件上传
     */
    @PostMapping(value = "/upload/multiple", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<FileUploadVO> uploadFile(MultiFileUploadParam param) {
        fileUploadService.uploadMultiFiles(param);
        return Result.ok();
    }

    /**
     * 文件下载，不通过静态资源访问
     * @return 文件字节
     */
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download(FileDownloadParam param) {
        return new ResponseEntity<>(new byte[]{}, HttpStatusCode.valueOf(200));
    }
}
