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
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据库类型
     *
     * @see DbType
     */
    private String dbType;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 连接名
     */
    private String connName;
    /**
     * URL
     */
    private String connUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    // @Encrypt
    private String password;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 驱动连接属性
     */
    @TableField(value = "driver_props", typeHandler = JacksonTypeHandler.class)
    private ObjectNode driverProps;

    /**
     * IP地址
     */
    @TableField(exist = false)
    private String ip;

    /**
     * 端口号
     */
    @TableField(exist = false)
    private Integer port;
}
