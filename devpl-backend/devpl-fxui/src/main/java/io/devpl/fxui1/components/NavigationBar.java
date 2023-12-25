package io.devpl.fxui1.components;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;

/**
 * 导航栏
 */
public class NavigationBar extends Region {

    private final TreeView<String> sideMenu;
    private final ScrollPane contentPane;

    public NavigationBar() {
        sideMenu = new TreeView<>();
        contentPane = new ScrollPane();
        // 自定义单元格
        sideMenu.setCellFactory(param -> new MenuTreeCell());
        sideMenu.setRoot(new TreeItem<>("Root"));
        sideMenu.setShowRoot(false);
        getChildren().addAll(sideMenu, contentPane);
    }

    @SuppressWarnings("unchecked")
    public <T extends Node> T getCurrentContent() {
        return (T) contentPane.getContent();
    }

    /**
     * 添加菜单
     *
     * @param menu 菜单
     */
    public final void addMenu(TreeMenu menu) {
        this.sideMenu.getRoot().getChildren().add(menu);
    }

    /**
     * 待完善
     */
    @Override
    protected void layoutChildren() {
        double height = this.getHeight();
        double width = this.getWidth();

        // 初始时取宽度的三份之一作为侧边栏的宽度
        double sideMenuWidth = sideMenu.getWidth() == 0 ? width / 3 : sideMenu.getWidth();

        layoutInArea(sideMenu, 0, 0, sideMenuWidth, height, 0, HPos.CENTER, VPos.CENTER);

        layoutInArea(contentPane, sideMenuWidth, 0, this.getWidth() - sideMenuWidth, height, 0, HPos.CENTER, VPos.CENTER);
    }

    class MenuTreeCell extends TreeCell<String> {

        TreeMenu menu;

        public MenuTreeCell() {
            setOnMouseClicked(event -> {
                if (this.menu == null) {
                    return;
                }
                if (this.menu.hasChildren()) {
                    // 目录节点无路由选项
                    return;
                }
                Object source = event.getSource();
                if (source instanceof MenuTreeCell cell) {
                    NavigationBar.this.contentPane.setContent(cell.menu.getRoutingNode());
                }
            });
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item);
                TreeItem<String> treeItem = getTreeItem();
                if (treeItem instanceof TreeMenu m) {
                    this.menu = m;
                }
            }
        }
    }
}
