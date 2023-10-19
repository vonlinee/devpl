package io.devpl.tookit.fxui.view.filestructure;

import io.devpl.tookit.fxui.view.IconKey;
import io.devpl.tookit.fxui.view.IconMap;

/**
 * 字段
 */
public class FieldItem extends JavaElementItem {

    private String name;

    public FieldItem() {
        super(IconMap.loadSVG(IconKey.JAVA_FIELD));
    }
}
