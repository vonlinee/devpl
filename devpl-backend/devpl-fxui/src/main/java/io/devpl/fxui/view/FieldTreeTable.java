package io.devpl.fxui.view;

import io.devpl.fxui.model.FieldNode;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.converter.DefaultStringConverter;

public class FieldTreeTable extends TreeTableView<FieldNode> {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    TreeItem<FieldNode> rootNode;

    public FieldTreeTable() {
        setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        setEditable(true);

        TreeTableColumn<FieldNode, String> nameCol = new TreeTableColumn<>("名称");
        TreeTableColumn<FieldNode, String> dataTypeCol = new TreeTableColumn<>("数据类型");
        TreeTableColumn<FieldNode, String> descCol = new TreeTableColumn<>("描述信息");

        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        dataTypeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("dataType"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));

        nameCol.setCellFactory(ttc -> {
            TextFieldTreeTableCell<FieldNode, String> cell = new TextFieldTreeTableCell<>(new DefaultStringConverter());
            cell.setEditable(true);
            return cell;
        });

        this.getColumns().add(nameCol);
        this.getColumns().add(dataTypeCol);
        this.getColumns().add(descCol);

        ContextMenu contextMenu = new ContextMenu();

        MenuItem addMenuItem = new MenuItem("新增");
        addMenuItem.setMnemonicParsing(true);
        addMenuItem.setOnAction(e -> {
            FieldNode newNode = new FieldNode();
            newNode.setName("New Field");
            newNode.setName("New Field");
            newNode.setName("String");
            rootNode.getChildren().add(new TreeItem<>(newNode));
        });
        contextMenu.getItems().add(addMenuItem);
        contextMenu.setWidth(300);
        contextMenu.setPrefWidth(300);
        // setContextMenu(contextMenu);

        setShowRoot(false);
        setRoot(rootNode = new TreeItem<>(null));

        this.setRowFactory(ttv -> {

            TreeTableRow<FieldNode> row = new TreeTableRow<>();
            // 给行添加右键菜单
            row.setOnContextMenuRequested(event -> {
                ContextMenu menu = new ContextMenu();

                @SuppressWarnings("unchecked")
                TreeTableRow<FieldNode> curRow = (TreeTableRow<FieldNode>) event.getSource();
                MenuItem menuItem1 = new MenuItem("添加子节点");
                menuItem1.setOnAction(e -> {
                    curRow.getTreeItem().getChildren().add(new TreeItem<>(new FieldNode("Unknown", "", "")));
                    curRow.getTreeItem().setExpanded(true);
                });
                MenuItem menuItem2 = new MenuItem("Menu Item 2");
                menuItem2.setOnAction(e -> {
                    // 处理菜单项点击事件
                });
                menu.getItems().addAll(menuItem1, menuItem2);
                row.setContextMenu(menu);
            });

            // 拖拽-检测
            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });
            //释放-验证
            row.setOnDragOver(event -> {
                // 拖拽到的行
                @SuppressWarnings("unchecked")
                TreeTableRow<FieldNode> toRow = (TreeTableRow<FieldNode>) event.getSource();
                Dragboard db = event.getDragboard();
                // check if same table view
                @SuppressWarnings("unchecked")
                TreeTableRow<FieldNode> sourceRow = (TreeTableRow<FieldNode>) event.getGestureSource();
                if (sourceRow.getTreeTableView() != row.getTreeTableView()) {
                    // 不能跨表拖拽
                    return;
                }
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != (Integer) db.getContent(SERIALIZED_MIME_TYPE)) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });
            //释放-执行
            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    TreeItem<FieldNode> removed = rootNode.getChildren().remove(draggedIndex);

                    int dropIndex;
                    if (row.isEmpty()) {
                        dropIndex = rootNode.getChildren().size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    rootNode.getChildren().add(dropIndex, removed);

                    event.setDropCompleted(true);
                    ttv.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });
            return row;
        });

        FieldNode f1 = new FieldNode("A", "A", "A");
        FieldNode f2 = new FieldNode("B", "B", "B");
        FieldNode f3 = new FieldNode("C", "C", "C");
        FieldNode f4 = new FieldNode("D", "D", "D");

        rootNode.getChildren().add(new TreeItem<>(f1));
        rootNode.getChildren().add(new TreeItem<>(f2));
        rootNode.getChildren().add(new TreeItem<>(f3));
        rootNode.getChildren().add(new TreeItem<>(f4));
    }
}
