package io.devpl.fxui.view;

import io.devpl.fxui.components.table.FXTableViewColumn;
import io.devpl.fxui.components.table.FXTableViewModel;
import lombok.Data;

@Data
@FXTableViewModel
public class TypeMappingItem {

    @FXTableViewColumn(title = "JSON")
    private String jsonType;

    @FXTableViewColumn(title = "JDBC")
    private String jdbcType;

    @FXTableViewColumn(title = "JAVA")
    private String javaType;
}