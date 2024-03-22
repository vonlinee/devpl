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
@TableName("data_type_mapping_group")
public class DataTypeMappingGroup {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据类型映射分组名称
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;
}
