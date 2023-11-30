package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基类信息
 */
@Data
@TableName("model_info")
public class ModelInfo {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 基类包名
     */
    private String packageName;
    /**
     * 基类编码
     */
    private String code;
    /**
     * 公共字段，多个用英文逗号分隔
     */
    @TableField(value = "fields")
    private String fieldsName;
    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 是否逻辑删除
     */
    @TableField(value = "is_deleted")
    private Boolean deleted;

    /**
     * 模型相关的字段列表
     */
    @TableField(exist = false)
    private List<ModelField> fields;
}
