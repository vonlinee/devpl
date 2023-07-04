package io.devpl.generator.domain.param;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 多文件上传参数
 */
@Getter
@Setter
public class MultiFileUploadParam extends FileUploadParam {

    /**
     * 上传的文件数组
     */
    private MultipartFile[] files;
}
