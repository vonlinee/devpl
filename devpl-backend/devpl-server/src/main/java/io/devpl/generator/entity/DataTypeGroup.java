package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTypeGroup extends DBEntityBase {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型分组ID
     */
    @TableField(value = "group_id")
    private String groupId;

    /**
     * 类型分组名称
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 是否内置类型分组，内置类型分组不可更改
     */
    @TableField(value = "internal")
    private Boolean internal;
}
