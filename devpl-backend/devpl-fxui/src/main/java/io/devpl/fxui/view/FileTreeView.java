package io.devpl.fxui.view;

import io.devpl.common.model.FileNode;
import io.devpl.common.utils.FileComparator;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 文件树结构
 */
public class FileTreeView extends TreeView<FileNode> {

    private File rootDirectory;
    private Comparator<File> comparator;

    public File getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public FileTreeView(File root) {
        setCellFactory(param -> new FileNodeTreeCell());
        setShowRoot(true);
        this.comparator = new FileComparator();
        updateRoot(this.rootDirectory = root);
    }

    public void updateRoot(File rootDirectory) {
        TreeItem<FileNode> root = getRoot();
        if (root != null) {
            root.setValue(newTreeNode(rootDirectory));
        } else {
            root = newTreeItem(rootDirectory);
            this.setRoot(root);
        }
    }

    private TreeItem<FileNode> newTreeItem(File file) {
        FileNode node = newTreeNode(file);
        TreeItem<FileNode> item = new TreeItem<>(node);
        if (node.isLeaf()) {
            Icon icon = new Icon("static/icon/svg/file.svg");
            icon.setPrefSize(16, 16);
            item.setGraphic(icon);
        } else {
            item.setGraphic(new Icon("static/icon/svg/folder.svg"));
        }
        return item;
    }

    private FileNode newTreeNode(File file) {
        FileNode fileNode = new FileNode();
        fileNode.setLeaf(!file.isDirectory());
        fileNode.setSelectable(true);
        fileNode.setPath(file.getAbsolutePath());
        fileNode.setName(file.getName());
        fileNode.setAbsolutePath(file.getAbsolutePath());
        return fileNode;
    }

    static class FileNodeTreeCell extends TreeCell<FileNode> {

        Icon icon;

        public FileNodeTreeCell() {
            setOnMouseClicked(event -> {
                TreeItem<FileNode> treeItem = getTreeItem();
                FileTreeView treeView = (FileTreeView) getTreeView();
                if (treeItem != null) {
                    FileNode fileNode = treeItem.getValue();
                    if (fileNode != null && !fileNode.isLeaf()
                        && treeItem.isLeaf() && treeItem.getChildren().size() == 0
                        && fileNode.getAbsolutePath() != null && !fileNode.getAbsolutePath().isEmpty()) {
                        // 加载子节点
                        File file = new File(fileNode.getAbsolutePath());
                        File[] files = file.listFiles();
                        if (files != null && files.length > 0) {
                            Arrays.sort(files, treeView.getComparator());
                            ObservableList<TreeItem<FileNode>> children = treeItem.getChildren();
                            for (File value : files) {
                                children.add(treeView.newTreeItem(value));
                            }
                        }
                    }
                }
            });
        }

        @Override
        protected void updateItem(FileNode item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getName());
                setGraphic(getTreeItem().getGraphic());
            }
        }
    }

    public final void setComparator(Comparator<File> comparator) {
        this.comparator = comparator;
    }

    public final Comparator<File> getComparator() {
        return comparator;
    }
}
