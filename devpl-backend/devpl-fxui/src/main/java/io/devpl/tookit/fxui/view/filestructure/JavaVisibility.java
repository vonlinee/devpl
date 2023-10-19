package io.devpl.tookit.fxui.view.filestructure;

import io.devpl.tookit.fxui.view.IconKey;
import io.devpl.tookit.fxui.view.IconMap;
import io.fxtras.svg.SVGImage;

/**
 * Java 可见性枚举
 */
public enum JavaVisibility {

    PUBLIC(IconKey.JAVA_PUBLIC),
    PRIVATE(IconKey.JAVA_PRIVATE),
    PROTECTED(IconKey.JAVA_PROTECTED),
    PACKAGE_VISIABLE(IconKey.JAVA_PLOCAL);

    /**
     * 对应展示的图标
     */
    private final String iconUrl;

    JavaVisibility(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public SVGImage getIconNode() {
        return IconMap.loadSVG(this.iconUrl);
    }
}
