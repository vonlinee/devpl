package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.devpl.generator.common.annotation.EncryptParam;
import io.devpl.generator.config.DbType;
import lombok.Data;

import java.util.Date;

/**
 * 数据源管理
 */
@Data
@TableName("gen_datasource")
public class GenDataSource {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 数据库类型
     * @see DbType
     */
    private String dbType;
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
    @EncryptParam
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;
}
