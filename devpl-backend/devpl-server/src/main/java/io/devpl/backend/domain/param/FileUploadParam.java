package io.devpl.backend.domain.param;

import jakarta.validation.constraints.NotBlank;

/**
 * 文件上传参数
 */
public class FileUploadParam {

    /**
     * 一般会有一个根目录
     * 存放子目录名称
     */
    @NotBlank(message = "保存子目录不能为空")
    private String folder;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
