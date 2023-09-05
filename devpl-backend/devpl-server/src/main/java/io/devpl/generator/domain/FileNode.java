package io.devpl.generator.domain;

import lombok.Data;

import java.util.List;

/**
 * 文件节点
 */
@Data
public class FileNode {

    /**
     * 树节点唯一key
     */
    private String key;

    /**
     * 文件名
     */
    private String label;

    /**
     * 是否是叶子结点
     */
    private Boolean isLeaf;

    /**
     * 是否可选中
     */
    private Boolean selectable;

    /**
     * 文件绝对路径
     */
    private String path;

    /**
     * 子节点
     */
    private List<FileNode> children;

    /**
     * 文件扩展名，如果是目录，则为null
     */
    private String extension;
}
