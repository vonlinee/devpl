package io.devpl.fxui.tools.mybatis;

public interface Visitor<T> {

    /**
     * 访问当前节点作为根节点的子树
     * @param tree 当前节点
     * @return Visitor实例
     */
    Visitor<T> visitTree(TreeNode<T> tree);

    void visitData(TreeNode<T> parent, T data);
}
