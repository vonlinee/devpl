package com.baomidou.mybatisplus.generator.query;

import com.baomidou.mybatisplus.generator.config.builder.Context;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedColumn;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 获取数据库的元数据信息
 */
public interface DatabaseIntrospector {

    /**
     * 设置上下文
     *
     * @param context 上下文对象
     */
    void setContext(Context context);

    /**
     * 获取表信息
     *
     * @param schemaPattern    schema
     * @param tableNamePattern 表名
     * @param tableTypes       表类型
     * @return 表信息
     */
    List<IntrospectedTable> getTables(String schemaPattern, String tableNamePattern, String[] tableTypes);

    /**
     * 获取列信息
     *
     * @param catalog           catalog
     * @param schemaPattern     数据库名称
     * @param tableNamePattern  表名称
     * @param columnNamePattern 列名称pattern
     * @return 列信息
     */
    List<IntrospectedColumn> getColumns(String catalog, String schemaPattern,
                                        String tableNamePattern, String columnNamePattern);
}
