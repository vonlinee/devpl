package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据类型对应关系关联表
 * <p>
 * 多对多关系
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
     * 数据类型映射分组ID
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 主数据类型ID，关联data_type_item表主键
     */
    @TableField(value = "type_id")
    private Long typeId;

    /**
     * 类型Key
     */
    @TableField(value = "type_key")
    private String typeKey;

    /**
     * 映射数据类型ID，关联data_type_item表主键
     */
    @TableField(value = "another_type_id")
    private Long anotherTypeId;

    /**
     * 映射数据类型Key
     */
    @TableField(value = "another_type_key")
    private String anotherTypeKey;

    public DataTypeMapping(Long typeId, Long anotherTypeId) {
        this.typeId = typeId;
        this.anotherTypeId = anotherTypeId;
    }
}
