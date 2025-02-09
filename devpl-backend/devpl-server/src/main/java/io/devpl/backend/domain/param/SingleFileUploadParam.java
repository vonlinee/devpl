package io.devpl.backend.domain.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 单文件上传，文件大小大于配置的分块大小时，进行分块上传，需要前后端配合
 */
@Getter
@Setter
public class SingleFileUploadParam extends SliceUploadParam {

    /**
     * 当"folder/filename"指向的文件存在时，是否覆盖文件，默认false
     */
    private boolean override = false;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空")
    private String filename;

    /**
     * 文件
     */
    @NotNull(message = "上传文件不能为空")
    private MultipartFile file;
}
