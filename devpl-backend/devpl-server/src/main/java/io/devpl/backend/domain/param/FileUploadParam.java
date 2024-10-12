package io.devpl.backend.domain.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Objects;

/**
 * 文件上传参数
 */
@Getter
public class FileUploadParam {

    /**
     * 一般会有一个根目录
     * 存放子目录名称
     */
    @NotBlank(message = "保存子目录不能为空")
    private String folder;

    public final void setFolder(String folder) {
        this.folder = Objects.requireNonNull(folder, "folder cannot be null");
    }
}
