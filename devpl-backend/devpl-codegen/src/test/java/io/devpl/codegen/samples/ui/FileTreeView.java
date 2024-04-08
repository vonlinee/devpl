package io.devpl.codegen.samples.ui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

/**
 * 文件树结构
 */
public class FileTreeView extends JScrollPane {

    JTree tree;
    Consumer<File> nodeSelectionHandler;

    public FileTreeView() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(null);
        DefaultTreeModel treeModel = new DefaultTreeModel(root, false); // 设置第二个参数为false，表示不展示根节点
        tree = new JTree(treeModel);
        tree.setShowsRootHandles(false);
        setViewportView(tree);

        setSize(400, 500);
        setMinimumSize(new Dimension(300, -1));
        // 添加节点点击事件监听器
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                // 在这里处理节点点击事件
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode != null) {
                    File file = (File) selectedNode.getUserObject();
                    if (file != null && nodeSelectionHandler != null) {
                        nodeSelectionHandler.accept(file);
                    }
                }
            }
        });
    }

    public void setRootDirectory(File rootDirectory) {
        FileNode rootDirNode = new FileNode(rootDirectory);
        UIHelper.runLater(() -> {
            createNodes(rootDirNode, rootDirectory, 0);
            getRootNode().add(rootDirNode);
            expandAll();
        });
    }

    private DefaultMutableTreeNode getRootNode() {
        return (DefaultMutableTreeNode) tree.getModel().getRoot();
    }

    private void createNodes(FileNode parentNode, File rootDir, int level) {
        if (rootDir.isDirectory()) {
            File[] files = rootDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    FileNode current = new FileNode(file);
                    parentNode.add(current);
                    createNodes(current, file, level + 1);
                }
            }
        }
    }

    public void expandAll() {
        // 展开所有节点
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    static class FileNode extends DefaultMutableTreeNode {

        private final File file;

        public FileNode(File file) {
            this.file = file;
            setUserObject(file);
        }

        @Override
        public String toString() {
            if (getParent() == getRoot()) {
                return file.getAbsolutePath();
            }
            return file.getName();
        }
    }

    public void setNodeSelectionHandler(Consumer<File> consumer) {
        this.nodeSelectionHandler = consumer;
    }
}
