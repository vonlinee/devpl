package io.devpl.fxui.view;

import io.devpl.fxui.components.table.FXTableViewColumn;
import io.devpl.fxui.components.table.FXTableViewModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 值生成器配置项
 */
@Setter
@Getter
@FXTableViewModel
public class Option {

    @FXTableViewColumn(title = "名称")
    private String name;

    @FXTableViewColumn(title = "值")
    private Object value;
}
