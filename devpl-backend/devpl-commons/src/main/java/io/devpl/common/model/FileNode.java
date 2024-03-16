package io.devpl.common.model;

import java.util.List;
import java.util.Objects;

/**
 * 文件节点
 *
 * @see java.io.File
 */
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
     * 文件名
     */
    private String name;

    /**
     * 是否是叶子结点
     */
    private boolean leaf = true;

    /**
     * 是否可选中
     */
    private boolean selectable;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件绝对路径
     */
    private String absolutePath;

    /**
     * 子节点
     */
    private List<FileNode> children;

    /**
     * 文件扩展名，如果是目录，则为null
     */
    private String extension;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
            int i = name.lastIndexOf(".");
            if (i >= 0) {
                this.extension = name.substring(i);
            }
        } else {
            this.name = "Unknown";
        }
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FileNode> getChildren() {
        return children;
    }

    public void setChildren(List<FileNode> children) {
        this.children = children;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileNode fileNode = (FileNode) o;
        return leaf == fileNode.leaf && selectable == fileNode.selectable && Objects.equals(key, fileNode.key) && Objects.equals(label, fileNode.label) && Objects.equals(name, fileNode.name) && Objects.equals(path, fileNode.path) && Objects.equals(children, fileNode.children) && Objects.equals(extension, fileNode.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, label, name, leaf, selectable, path, children, extension);
    }

    @Override
    public String toString() {
        return "FileNode{" +
            "key='" + key + '\'' +
            ", label='" + label + '\'' +
            ", name='" + name + '\'' +
            ", leaf=" + leaf +
            ", selectable=" + selectable +
            ", path='" + path + '\'' +
            ", children=" + children +
            ", extension='" + extension + '\'' +
            '}';
    }
}
