package io.devpl.fxui.tools.mybatis;

import java.util.LinkedList;
import java.util.List;

/**
 * 树节点
 *
 * @param <T>
 */
public class TreeNode<T> implements Visitable<T> {
    // LinkedHashSet保持插入顺序
    private List<TreeNode<T>> children = new LinkedList<>();
    private T data;

    /**
     * 父节点
     */
    private TreeNode<T> parent;

    public TreeNode(T data) {
        this.data = data;
    }

    @Override
    public void accept(Visitor<T> visitor) {
        // 当前节点
        visitor.visitData(this, data);
        // 子节点
        visitChildren(visitor);
    }

    protected void visitChildren(Visitor<T> visitor) {
        for (TreeNode<T> child : children) {
            // 遍历完一颗子树，产生一个新的Visitor
            Visitor<T> newVisitor = visitor.visitTree(child);
            child.accept(newVisitor);
        }
    }

    public TreeNode<T> addChild(T data) {
        for (TreeNode<T> child : children) {
            if (child.data.equals(data)) {
                return child;
            }
        }
        return addChild(new TreeNode<>(data));
    }

    public TreeNode<T> addChild(TreeNode<T> child) {
        children.add(child);
        return child;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }
}
