package io.devpl.generator.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传结果
 */
@Data
public class FileUploadResult {

    /**
     * 文件路径
     */
    private List<String> pathList = new ArrayList<>();

    public void addFile(String path) {
        pathList.add(path);
    }
}
