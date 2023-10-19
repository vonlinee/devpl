package io.fxtras.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * 被标签包围的一个区域
 * @see javafx.scene.control.ScrollPane
 * @see javafx.scene.layout.AnchorPane
 */
public class TaggedRegion extends Region {

    /**
     * 标签文本
     */
    Text tagNode;

    public TaggedRegion(String text) {
        tagNode = new Text();
        tagNode.setText(text);
        getChildren().add(tagNode);
    }

    public final String getText() {
        return tagNode.getText();
    }

    public final StringProperty textProperty() {
        return tagNode.textProperty();
    }

    public final void setText(String text) {
        tagNode.setText(text);
    }

    /**
     * The node used as the content of this TaggedRegion.
     */
    private ObjectProperty<Node> content;

    public final void setContent(Node value) {
        contentProperty().set(value);
    }

    public final Node getContent() {
        return content == null ? null : content.get();
    }

    public final ObjectProperty<Node> contentProperty() {
        if (content == null) {
            content = new SimpleObjectProperty<>(this, "content");
        }
        return content;
    }

    /**
     * 绝对布局
     */
    @Override
    protected void layoutChildren() {
        tagNode.resizeRelocate(0, 0, 200, 200);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
    }
}
