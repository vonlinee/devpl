package io.devpl.codegen.db.query;

import io.devpl.codegen.core.ContextImpl;
import io.devpl.codegen.core.TableGeneration;
import io.devpl.codegen.core.ColumnGeneration;

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
    void setContext(ContextImpl context);

    /**
     * 获取表信息
     *
     * @param catalog          目录名。可以传入空字符串("")来获取所有的表，或者传入特定的目录名来获取该目录下的表。
     * @param schemaPattern    模式名模式。可以传入空字符串("")来获取所有的模式，或者传入特定的模式名模式来获取该模式下的表。
     * @param tableNamePattern 表名模式。可以传入特定的表名来获取该表，或者传入空字符串("")来获取所有的表。 %表示所有表
     * @param tableTypes       表类型
     * @return 表信息
     */
    List<TableGeneration> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] tableTypes);

    /**
     * 获取列信息
     *
     * @param catalog           目录名。可以传入空字符串("")来获取所有的表，或者传入特定的目录名来获取该目录下的表。
     * @param schemaPattern     数据库名称
     * @param tableNamePattern  表名称
     * @param columnNamePattern 列名称pattern
     * @return 列信息
     */
    List<ColumnGeneration> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern);
}
