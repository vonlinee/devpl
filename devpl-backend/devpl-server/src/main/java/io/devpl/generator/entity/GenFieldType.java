package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 字段类型管理
 */
@Data
@TableName("gen_field_type")
public class GenFieldType {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * 属性包名
     */
    private String packageName;

    /**
     * MySQL数据类型
     */
    @TableField(value = "mysql_sql_type")
    private String mysqlSqlType;

    /**
     * JSON数据类型
     */
    @TableField(value = "json_type")
    private String jsonType;

    /**
     * 创建时间
     */
    private Date createTime;
}
