package io.fxtras.component;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class TreeDisclosureNode extends Region {

    private final TreeCell<?> treeCell;

    Rectangle rectangle = new Rectangle();

    public TreeDisclosureNode(TreeCell<?> treeCell) {
        this.treeCell = treeCell;
        getChildren().add(rectangle);
        rectangle.setWidth(5);
        rectangle.setHeight(6);
        rectangle.setStyle("-fx-background-color: green");
    }

    @Override
    protected void layoutChildren() {
        double height = this.getHeight();
        double width = this.getWidth();

        System.out.println(this.getBoundsInParent());


        layoutInArea(rectangle, 0, 0, 0, 0, 0, HPos.CENTER, VPos.CENTER);
    }
}
