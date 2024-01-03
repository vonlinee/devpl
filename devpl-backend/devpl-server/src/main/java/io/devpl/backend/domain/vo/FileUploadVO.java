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
     * 文件路径
     */
    private List<String> pathList = new ArrayList<>();

    public final void addFilePath(String path) {
        pathList.add(path);
    }
}
