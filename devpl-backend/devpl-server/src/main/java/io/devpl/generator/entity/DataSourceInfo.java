package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.devpl.generator.common.annotation.Encrypt;
import io.devpl.generator.config.DbType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 数据源管理
 */
@Data
@TableName("datasource_info")
public class DataSourceInfo {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据库类型
     */
    @TableField(value = "db_type")
    private String dbType;

    /**
     * IP地址
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 端口号
     */
    @TableField(value = "port")
    private Integer port;

    /**
     * 驱动类名
     */
    @TableField(value = "driver_class_name")
    private String driverClassName;

    /**
     * 数据库名称
     */
    @TableField(value = "database_name")
    private String databaseName;

    /**
     * 连接名
     */
    @TableField(value = "conn_name")
    private String connName;

    /**
     * URL
     */
    @TableField(value = "conn_url")
    private String connUrl;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 驱动属性
     */
    @TableField(value = "driver_props")
    private String driverProps;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否逻辑删除
     */
    @TableField(value = "is_deleted")
    private Boolean deleted;
}
