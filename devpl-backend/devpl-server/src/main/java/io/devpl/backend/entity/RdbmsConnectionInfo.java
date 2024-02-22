package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.devpl.codegen.db.JDBCDriver;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * 关系型数据库连接信息
 */
@Getter
@Setter
@TableName("rdbms_connection_info")
public class RdbmsConnectionInfo implements Serializable {
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
    @Nullable
    @TableField(value = "db_name")
    private String dbName;

    /**
     * 连接名
     */
    @TableField(value = "connection_name")
    private String connectionName;

    /**
     * URL
     */
    @TableField(value = "connection_url")
    private String connectionUrl;

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
    @TableField(value = "driver_props", typeHandler = JacksonTypeHandler.class)
    private Properties driverProperties;

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

    public String buildConnectionUrl() {
        return buildConnectionUrl(this.dbName);
    }

    public String buildConnectionUrl(String databaseName) {
        String connectionUrl = null;
        if (this.driverClassName != null) {
            JDBCDriver driver = JDBCDriver.findByDriverClassName(this.driverClassName);
            connectionUrl = driver.getConnectionUrl(this.host, this.port, databaseName, this.driverProperties);
        }
        return connectionUrl;
    }
}
