package org.apache.ddlutils.platform;

/**
 * 数据库类型
 * 部分数据库的不同版本也视为不同的类型
 */
public interface DBType {

    /**
     * 数据库名称
     *
     * @return 数据库名称
     */
    String getName();

    /**
     * 版本号
     *
     * @return 版本号
     */
    default String getVersion() {
        return "UNKNOWN";
    }

    /**
     * 注册驱动类型
     *
     * @param driverType 驱动类型
     */
    default void registerDriver(JDBCDriverType driverType) {
        throw new UnsupportedOperationException("this method is not implemented");
    }

    JDBCDriverType[] getSupportedDrivers();
}
