package io.devpl.fxui.view;

import io.devpl.fxui.components.table.FXTableViewColumn;
import io.devpl.fxui.components.table.FXTableViewModel;

/**
 * 值生成器配置项
 */
@FXTableViewModel
public class Option {

    @FXTableViewColumn(title = "名称")
    private String name;

    @FXTableViewColumn(title = "值")
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
