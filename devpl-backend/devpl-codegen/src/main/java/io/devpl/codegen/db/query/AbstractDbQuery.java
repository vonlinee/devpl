package io.devpl.codegen.db.query;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表数据查询抽象类
 */
public abstract class AbstractDbQuery {
    /**
     * 表信息查询 SQL
     */
    public abstract String tablesSql();

    /**
     * 表字段信息查询 SQL
     */
    public abstract String tableFieldsSql();

    /**
     * 表名称
     */
    public abstract String tableName();

    /**
     * 表注释
     */
    public abstract String tableComment();

    /**
     * 字段名称
     */
    public abstract String fieldName();

    /**
     * 字段类型
     */
    public abstract String fieldType();

    /**
     * 字段注释
     */
    public abstract String fieldComment();

    /**
     * 主键字段
     */
    public abstract String fieldKey();

    /**
     * 判断主键是否为identity
     *
     * @param results ResultSet
     * @return 主键是否为identity
     * @throws SQLException ignore
     */
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return false;
    }

    /**
     * 自定义字段名称
     */
    public String[] fieldCustom() {
        return null;
    }
}
