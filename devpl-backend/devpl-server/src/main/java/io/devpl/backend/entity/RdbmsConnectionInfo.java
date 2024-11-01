package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.devpl.sdk.util.PropertiesUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.apache.ddlutils.platform.BuiltinDriverType;
import org.apache.ddlutils.platform.DatabaseType;
import org.apache.ddlutils.platform.DriverType;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * 关系型数据库连接信息
 */
@Getter
@Setter
@TableName(value = "rdbms_connection_info", autoResultMap = true)
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
     * 需要指定autoResultMap = true
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
     * @see BuiltinDriverType#name()
     */
    @TableField(value = "driver_type")
    private String driverType;

    public String buildConnectionUrl() {
        return buildConnectionUrl(this.dbName);
    }

    public String buildConnectionUrl(String databaseName) {
        String connectionUrl = null;
        if (this.driverClassName != null) {
            BuiltinDriverType driver = BuiltinDriverType.findByDriverClassName(this.driverClassName);
            connectionUrl = driver.getConnectionUrl(this.host, this.port, databaseName, this.driverProperties);
        }
        return connectionUrl;
    }

    public RdbmsConnectionInfo() {
        this(null);
    }

    public RdbmsConnectionInfo(String connectionUrl) {
        this.deleted = false;
        this.setConnectionUrl(connectionUrl, true);
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public void setConnectionUrl(String connectionUrl) {
        setConnectionUrl(connectionUrl, false);
    }

    public void setConnectionUrl(String connectionUrl, boolean init) {
        this.connectionUrl = connectionUrl;
        if (init) {
            updateByConnectionUrl(connectionUrl);
        }
    }

    public void updateByConnectionUrl(String connectionUrl) {
        if (connectionUrl != null) {
            try {
                int left = 0;
                final int right = connectionUrl.length();
                // 数据库类型
                final char[] connectionUrlCharArray = connectionUrl.toCharArray();
                // JDBC协议部分
                for (int i = left; i < right; i++) {
                    if (connectionUrlCharArray[i] == ':') {
                        left = i + 1;
                        break;
                    }
                }
                // 数据库类型解析
                for (int i = left; i < right; i++) {
                    if (connectionUrlCharArray[i] == ':') {
                        String dbType = new String(connectionUrlCharArray, left, i - left);
                        DatabaseType dbTypeEnum = BuiltinDatabaseType.getValue(dbType, BuiltinDatabaseType.MYSQL);
                        this.setDbType(dbTypeEnum.getName().toLowerCase());
                        DriverType driverType = dbTypeEnum.getSupportedDriverTypes()[0];
                        if (driverType == null) {
                            this.setDriverClassName(driverType.getDriverClassName());
                            driverType = BuiltinDriverType.MYSQL5;
                        }
                        this.driverType = driverType.getName();

                        left = rightMoveToLetterOrNumber(right, connectionUrlCharArray, i);
                        break;
                    }
                }
                // IP地址
                for (int i = left; i < right; i++) {
                    if (connectionUrlCharArray[i] == ':') {
                        String ip = new String(connectionUrlCharArray, left, i - left);
                        this.setHost(ip);
                        left = rightMoveToLetterOrNumber(right, connectionUrlCharArray, i);
                        break;
                    }
                }
                // 端口号
                for (int i = left; i < right; i++) {
                    if (connectionUrlCharArray[i] == '/') {
                        String port = new String(connectionUrlCharArray, left, i - left);
                        this.setPort(Integer.parseInt(port));
                        left = rightMoveToLetterOrNumber(right, connectionUrlCharArray, i);
                        break;
                    }
                }
                // 数据库名称
                for (int i = left; i < right; i++) {
                    if (connectionUrlCharArray[i] == '?') {
                        String databaseName = new String(connectionUrlCharArray, left, i - left);
                        this.setDbName(databaseName);
                        left = rightMoveToLetterOrNumber(right, connectionUrlCharArray, i);
                        break;
                    }
                }
                // 连接参数部分
                this.setDriverProperties(PropertiesUtils.parse(new String(connectionUrlCharArray, left, right - left)));
            } catch (Exception exception) {
                throw new RuntimeException("cannot parse connection info from connection url", exception);
            }
        }
    }

    private int rightMoveToLetterOrNumber(int right, char[] connectionUrlCharArray, int i) {
        int left;
        left = i;
        for (int j = i + 1; j < right; j++) {
            if (Character.isLetter(connectionUrlCharArray[j]) || Character.isDigit(connectionUrlCharArray[j])) {
                left = j;
                break;
            } else {
                j++;
            }
        }
        return left;
    }
}
