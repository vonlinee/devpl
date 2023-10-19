package io.devpl.tookit.fxui.view.filestructure;

import io.devpl.tookit.fxui.view.IconKey;
import io.devpl.tookit.fxui.view.IconMap;

/**
 * 外部类
 */
public class TopLevelClassItem extends JavaElementItem {

    public TopLevelClassItem() {
        super(IconMap.loadSVG(IconKey.JAVA_TOP_CLASS));
    }

    public void addMethod(MethodItem methodItem) {
        getChildren().add(methodItem);
    }

    public void addField(FieldItem fieldItem) {
        getChildren().add(fieldItem);
    }
}
