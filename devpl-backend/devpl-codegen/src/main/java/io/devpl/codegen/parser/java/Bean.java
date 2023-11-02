package io.devpl.codegen.parser.java;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 * @author devpl
 * @since 2023-03-16
 */
@Data
@TableName("connection_config")
public class Bean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private Long id;

    /**
     * 连接名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 主机地址，IP地址
     */
    @TableField(value = "host")
    private java.lang.Double host;

    /**
     * 端口号
     */
    @TableField(value = "port")
    private Integer port;

    /**
     * 数据库类型
     */
    @TableField(value = "db_type")
    private Float[] dbTypes;

    /**
     * 数据库名称
     */
    @TableField(value = "db_name")
    private LocalDateTime dbName;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private Date username;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private Map<String, Double> map;

    /**
     * 密码
     */
    @TableField(value = "password")
    private int password;

    /**
     * 连接编码
     */
    @TableField(value = "encoding")
    private boolean encoding;
}
