package io.fxtras;

import javafx.scene.control.Control;

/**
 * FX控件类型枚举
 * <p>
 * 命名规范参考：<a href="https://blog.csdn.net/vipzjyno1/article/details/23542617">...</a>
 * <p>
 * 程序中使用单词缩写原则：不要用缩写，除非该缩写是约定俗成的。
 * 一:控件命名：控件类型 + 名称，整体按小驼峰命名
 * Button           btn
 * CheckBox         chb
 * TableView        tv
 * ImageView        imgView 或者 iv 或者 imgv
 * TextArea         tta
 * <p>
 * 容器类命名
 * BorderPane       bop
 * StackPane        stp
 * TabPane          tabp
 * TilePane         tilp
 * Group            grp
 * FlowPane         flp
 */
public final class ControlType {

    /**
     * 类型全类名
     */
    private Class<? extends Control> type;

    /**
     * 别名，比如TextArea => ta
     */
    private String alias;
}
