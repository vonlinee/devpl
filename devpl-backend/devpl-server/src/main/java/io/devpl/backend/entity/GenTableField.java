package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 生成配置信息-表字段
 */
@Getter
@Setter
@TableName("gen_table_field")
public class GenTableField {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 表ID
     */
    @TableField(value = "table_id")
    private Long tableId;

    /**
     * 字段名称
     */
    @TableField(value = "field_name")
    private String fieldName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 字段说明
     */
    @TableField(value = "field_comment")
    private String fieldComment;

    /**
     * 属性名
     */
    private String attrName;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * 属性包名
     */
    private String packageName;

    /**
     * 自动填充
     */
    private String autoFill;

    /**
     * 主键 0：否  1：是
     */
    private boolean primaryKey;

    /**
     * 基类字段 0：否  1：是
     */
    private boolean baseField;

    /**
     * 表单项 0：否  1：是
     */
    private boolean formItem;

    /**
     * 表单必填 0：否  1：是
     */
    private boolean formRequired;

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 表单字典类型
     */
    private String formDict;

    /**
     * 表单效验
     */
    private String formValidator;

    /**
     * 列表项 0：否  1：是
     */
    private boolean gridItem;

    /**
     * 列表排序 0：否  1：是
     */
    private boolean gridSort;

    /**
     * 查询项 0：否  1：是
     */
    private boolean queryItem;

    /**
     * 查询方式
     */
    private String queryType;

    /**
     * 查询表单类型
     */
    private String queryFormType;
}
