package io.devpl.fxui.components;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import org.fxmisc.richtext.CodeArea;

/**
 * 代码区域
 */
public final class CodeRegion extends Region {

    CodeArea codeArea;

    public CodeRegion() {
        codeArea = new CodeArea();
        codeArea.setStyle("-fx-font-family: consolas; -fx-font-size: 12pt;");
        codeArea.setWrapText(false);
        getChildren().add(codeArea);
    }

    @Override
    protected void layoutChildren() {
        layoutInArea(codeArea, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
    }
}
