package io.devpl.generator.controller;

import io.devpl.generator.common.FileStorageStrategy;
import io.devpl.generator.common.utils.JSONUtils;
import io.devpl.generator.common.utils.Result;
import io.devpl.generator.domain.param.SingleUploadParam;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
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
    FileStorageStrategy fileStorageStrategy;

    /**
     * 单文件上传
     */
    @PostMapping(value = "/upload/single", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Object> upload(SingleUploadParam param) {
        System.out.println(JSONUtils.toJsonString(param));

        return Result.ok(param);
    }
}
