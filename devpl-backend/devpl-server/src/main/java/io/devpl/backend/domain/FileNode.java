package io.devpl.backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 文件节点
 */
@Getter
@Setter
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
    private boolean leaf = true;

    /**
     * 是否可选中
     */
    private boolean selectable = true;

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
