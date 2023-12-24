package io.devpl.fxui.view;

import io.devpl.fxui.controls.FXUtils;
import io.devpl.fxui.model.FieldNode;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.Collections;
import java.util.List;

/**
 * 字段解析UI界面
 */
abstract class FieldParseView extends Region {

    Node root;

    public FieldParseView() {
        this.root = createRootNode();
        if (this.root != null)
            getChildren().add(root);
    }

    abstract String getName();


    /**
     * 创建UI部分
     *
     * @return UI根节点
     */
    abstract Node createRootNode();

    /**
     * 获取可解析的文本
     *
     * @return 可解析的文本
     */
    public abstract String getParseableText();

    /**
     * 填充示例文本
     */
    public abstract void fillSampleText();

    /**
     * 获取示例文本
     *
     * @return 示例文本
     */
    public abstract String getSampleText();

    /**
     * 解析文本得到字段
     *
     * @return 字段信息
     */
    public List<FieldNode> parse(String text) {
        return Collections.emptyList();
    }

    @Override
    protected void layoutChildren() {
        if (root != null) {
            FXUtils.layoutRoot(this, root);
        }
    }
}
