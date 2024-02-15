package io.devpl.backend.domain.vo;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class FileItem {

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件或者目录名称
     */
    private String name;

    /**
     * 绝对路径
     */
    private String absolutePath;

    /**
     * 子项目
     */
    private List<FileItem> children;

    private boolean leaf;

    public FileItem(File file) {
        if (file.isDirectory()) {
            this.type = "-1";
            this.leaf = false;
        } else {
            this.leaf = true;
        }
        this.absolutePath = file.getAbsolutePath();
        this.name = file.getName();
        if (this.name.isEmpty()) {
            this.name = this.absolutePath;
        }
    }
}
