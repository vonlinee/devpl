package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传结果
 */
@Getter
@Setter
public class FileUploadVO {

    /**
     * 上传后的文件路径
     */
    private String path;

    /**
     * 文件路径列表
     */
    private List<String> pathList = new ArrayList<>();

    public final void addFilePath(String path) {
        pathList.add(path);
    }
}
