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
}
