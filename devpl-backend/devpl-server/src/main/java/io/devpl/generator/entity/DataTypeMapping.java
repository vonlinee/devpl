package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据类型对应关系关联表
 */
@Data
@TableName("data_type_mapping")
public class DataTypeMapping {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "type_id")
    private Long typeId;

    @TableField(value = "another_type_id")
    private Long anotherTypeId;

    public DataTypeMapping(Long typeId, Long anotherTypeId) {
        this.typeId = typeId;
        this.anotherTypeId = anotherTypeId;
    }
}
