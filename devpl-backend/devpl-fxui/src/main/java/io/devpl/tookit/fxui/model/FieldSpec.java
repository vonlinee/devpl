package io.devpl.tookit.fxui.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.fxtras.component.TableViewColumn;
import io.fxtras.component.TableViewModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字段信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "field_spec")
@TableViewModel(name = "field_spec")
public class FieldSpec {

    /**
     * 是否选中，非数据库字段
     */
    @TableViewColumn(name = "是否选中")
    @TableField(exist = false)
    private boolean selected;

    /**
     * 主键ID
     */
    @TableId(value = "field_id", type = IdType.ASSIGN_ID)
    @TableViewColumn(name = "主键ID")
    private String fieldId;

    /**
     * 字段名称
     */
    @TableViewColumn(name = "字段名")
    @TableField(value = "field_name")
    private String fieldName;

    /**
     * 数据类型
     */
    @TableViewColumn(name = "数据类型")
    @TableField(value = "data_type")
    private String dataType;

    /**
     * 字段值
     */
    @TableField(value = "field_value")
    @TableViewColumn(name = "字段值")
    private String fieldValue;

    /**
     * 字段含义
     */
    @TableViewColumn(name = "描述")
    @TableField(value = "description")
    private String fieldDescription;
}
