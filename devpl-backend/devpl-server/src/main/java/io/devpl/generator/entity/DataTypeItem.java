package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 数据类型表
 */
@Data
@TableName("data_type_item")
public class DataTypeItem {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型分组名称
     */
    @TableField(value = "type_group_id")
    private String typeGroupId;

    /**
     * 类型ID
     */
    @TableField(value = "type_key")
    private String typeKey;

    /**
     * 类型名称
     */
    @TableField(value = "type_name")
    private String typeName;

    /**
     * 该数据类型的值类型
     */
    @TableField(value = "value_type")
    private String valueType;

    /**
     * 最小长度
     */
    @TableField(value = "min_length")
    private Integer minLength;

    /**
     * 最大长度
     */
    @TableField(value = "max_length")
    private Integer maxLength;

    /**
     * 类型默认值
     */
    @TableField(value = "default_value")
    private String defaultValue;

    /**
     * 精度 precision是mysql关键字
     */
    @TableField(value = "`precision`")
    private String precision;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", jdbcType = JdbcType.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", jdbcType = JdbcType.TIME)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    private Boolean deleted;

    /**
     * 类型分组名臣
     */
    @TableField(exist = false)
    private String typeGroupName;
}
