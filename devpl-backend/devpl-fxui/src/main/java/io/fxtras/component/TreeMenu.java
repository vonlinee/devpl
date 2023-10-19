package io.fxtras.component;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class TreeMenu extends TreeItem<String> {

    Node content;

    public TreeMenu(String menuName, Node content) {
        super(menuName);
        this.content = content;
    }

    /**
     * add sub menu
     *
     * @param menu 子菜单项
     */
    public final void addChild(TreeMenu menu) {
        if (menu.getParent() != null) {
            return;
        }
        this.getChildren().add(menu);
    }

    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    public Node getRoutingNode() {
        return this.content;
    }
}
