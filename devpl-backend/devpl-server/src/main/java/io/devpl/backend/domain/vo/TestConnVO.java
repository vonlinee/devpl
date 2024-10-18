package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 类似Idea的测试连接结果
 */
@Getter
@Setter
public class TestConnVO {

    /**
     * 是否失败
     */
    private boolean failed;

    /**
     * 数据库类型
     */
    private String dbmsType;

    /**
     * 是否使用ssl连接
     */
    private boolean useSsl;

    /**
     * 连接失败时的错误信息
     */
    private String errorMsg;

    private String productName;
    private String productVersion;
    private String driverName;
    private String driverVersion;
    private String userName;
    private String databaseMajorVersion;
    private String databaseMinorVersion;
    private int jdbcMajorVersion;
    private int jdbcMinorVersion;
}
