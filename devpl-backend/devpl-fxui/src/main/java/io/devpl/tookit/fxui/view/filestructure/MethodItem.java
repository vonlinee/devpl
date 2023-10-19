package io.devpl.tookit.fxui.view.filestructure;

import io.devpl.tookit.fxui.view.IconKey;
import io.devpl.tookit.fxui.view.IconMap;

public class MethodItem extends JavaElementItem {

    public MethodItem() {
        super(IconMap.loadSVG(IconKey.JAVA_METHOD));
    }
}
