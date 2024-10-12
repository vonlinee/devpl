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
     * 已完成数量
     */
    private int finished;

    /**
     * 总文件数
     */
    private int total;

    /**
     * 上传进度
     */
    private float progress;

    /**
     * 文件路径列表
     */
    private List<String> pathList;

    public final void addFilePath(String path) {
        if (pathList == null) {
            pathList = new ArrayList<>();
        }
        pathList.add(path);
    }

    public String getPath() {
        if (pathList == null || pathList.isEmpty()) {
            return null;
        }
        return pathList.get(0);
    }
}
