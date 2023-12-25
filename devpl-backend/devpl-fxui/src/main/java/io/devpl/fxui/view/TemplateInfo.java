package io.devpl.fxui.view;

import io.devpl.fxui.components.table.FXTableViewColumn;
import io.devpl.fxui.components.table.FXTableViewModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 模板信息
 */
@Getter
@Setter
@FXTableViewModel
public class TemplateInfo {

    /**
     * 模板名称：唯一
     */
    @FXTableViewColumn(title = "模板名称")
    private String templateName;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 备注信息
     */
    @FXTableViewColumn(title = "备注信息")
    private String remark;
}

