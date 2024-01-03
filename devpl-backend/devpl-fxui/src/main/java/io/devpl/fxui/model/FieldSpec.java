package io.devpl.fxui.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.devpl.fxui.components.table.FXTableViewColumn;
import io.devpl.fxui.components.table.FXTableViewModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * 字段信息
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "field_spec")
@FXTableViewModel
public class FieldSpec {

    /**
     * 是否选中，非数据库字段
     */
    @FXTableViewColumn(title = "是否选中")
    @TableField(exist = false)
    private boolean selected;

    /**
     * 主键ID
     */
    @TableId(value = "field_id", type = IdType.ASSIGN_ID)
    @FXTableViewColumn(title = "主键ID")
    private String fieldId;

    /**
     * 字段名称
     */
    @FXTableViewColumn(title = "字段名")
    @TableField(value = "field_name")
    private String fieldName;

    /**
     * 数据类型
     */
    @FXTableViewColumn(title = "数据类型")
    @TableField(value = "data_type")
    private String dataType;

    /**
     * 字段值
     */
    @TableField(value = "field_value")
    @FXTableViewColumn(title = "字段值")
    private String fieldValue;

    /**
     * 字段含义
     */
    @FXTableViewColumn(title = "描述")
    @TableField(value = "description")
    private String fieldDescription;
}
