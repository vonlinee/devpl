package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据类型对应关系关联表
 */
@Getter
@Setter
@TableName("data_type_mapping")
public class DataTypeMapping {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 主数据类型ID，关联data_type_item表主键
     */
    @TableField(value = "type_id")
    private Long typeId;

    /**
     * 映射数据类型ID，关联data_type_item表主键
     */
    @TableField(value = "another_type_id")
    private Long anotherTypeId;

    public DataTypeMapping(Long typeId, Long anotherTypeId) {
        this.typeId = typeId;
        this.anotherTypeId = anotherTypeId;
    }
}
