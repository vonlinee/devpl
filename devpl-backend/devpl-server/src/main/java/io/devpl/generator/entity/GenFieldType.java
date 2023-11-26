package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 字段类型管理
 */
@Getter
@Setter
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
    @TableField(value = "mysql_sql_type", exist = false)
    private String mysqlSqlType;

    /**
     * JSON数据类型
     */
    @TableField(value = "json_type", exist = false)
    private String jsonType;

    /**
     * 创建时间
     */
    private Date createTime;
}
