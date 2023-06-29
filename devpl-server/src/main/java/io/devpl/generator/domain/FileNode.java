package io.devpl.generator.domain;

import lombok.Data;

import java.util.List;

/**
 * 文件节点
 */
@Data
public class FileNode {

    private String key;

    private String label;

    private Boolean isLeaf;

    private Boolean selectable;

    /**
     * 文件绝对路径
     */
    private String path;

    private List<FileNode> children;
}
