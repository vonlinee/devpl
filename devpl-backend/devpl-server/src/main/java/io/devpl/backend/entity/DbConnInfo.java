package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.devpl.codegen.db.JDBCDriver;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据库连接信息管理
 */
@Getter
@Setter
@TableName("database_connection_info")
public class DbConnInfo implements Serializable {
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
    @TableField(value = "host")
    private String host;

    /**
     * 连接端口号
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
    @TableField(value = "db_name")
    private String dbName;

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
     * 密码,加密存储
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
    private LocalDateTime createTime;

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

    /**
     * 驱动类型
     *
     * @see JDBCDriver#name()
     */
    @TableField(value = "driver_type")
    private String driverType;
}
