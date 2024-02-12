package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * MyBatis Mapper语句记录表
 */
@Getter
@Setter
@TableName("mapped_statement_item")
public class MappedStatementItem {
    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 语句ID
     */
    @TableField(value = "belong_file")
    private String belongFile;

    /**
     * 语句ID
     */
    @TableField(value = "statement_id")
    private String statementId;

    /**
     * 语句类型
     */
    @TableField(value = "statement_type")
    private String statementType;

    /**
     * 命名空间
     */
    @TableField(value = "namespace")
    private String namespace;

    /**
     * 参数类型
     */
    @TableField(value = "param_type")
    private String paramType;

    /**
     * 结果类型
     */
    @TableField(value = "result_type")
    private String resultType;

    /**
     * 语句内容
     */
    @TableField(value = "statement")
    private String statement;

    /**
     * 是否逻辑删除
     */
    @TableField(value = "is_deleted")
    private Integer deleted;

}
