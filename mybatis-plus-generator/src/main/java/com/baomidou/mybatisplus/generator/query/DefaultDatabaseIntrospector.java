package com.baomidou.mybatisplus.generator.query;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedColumn;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedTable;
import com.baomidou.mybatisplus.generator.type.TypeConverter;
import com.baomidou.mybatisplus.generator.util.JdbcUtils;
import com.baomidou.mybatisplus.generator.util.StringUtils;
import com.baomidou.mybatisplus.generator.jdbc.meta.ColumnMetadata;
import com.baomidou.mybatisplus.generator.jdbc.meta.PrimaryKey;
import com.baomidou.mybatisplus.generator.jdbc.meta.TableMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 元数据查询数据库信息.
 * @author nieqiurong 2022/5/11.
 * @see TypeConverter 类型转换器(如果默认逻辑不能满足，可实现此接口重写类型转换)
 * <p>
 * 测试通过的数据库：H2、Mysql-5.7.37、Mysql-8.0.25、PostgreSQL-11.15、PostgreSQL-14.1、Oracle-11.2.0.1.0、DM8
 * </p>
 * <p>
 * FAQ:
 * 1.Mysql无法读取表注释: 链接增加属性 remarks=true&useInformationSchema=true 或者通过{@link DataSourceConfig.Builder#addConnectionProperty(String, String)}设置
 * 2.Oracle无法读取注释: 增加属性remarks=true，也有些驱动版本说是增加remarksReporting=true {@link DataSourceConfig.Builder#addConnectionProperty(String, String)}
 * </p>
 * @since 3.5.3
 */
public class DefaultDatabaseIntrospector extends AbstractDatabaseIntrospector {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 查询需要生成的所有表信息
     * @return 所有表信息
     */
    @Override
    public List<IntrospectedTable> getTables(String schemaPattern, String tableNamePattern, String[] tableTypes) {

        StrategyConfig strategyConfig = this.context.getStrategyConfig();
        DataSourceConfig dataSourceConfig = this.context.getDataSourceConfig();
        GlobalConfig globalConfig = this.context.getGlobalConfig();

        final Set<String> excludeTables = strategyConfig.getExclude();
        final Set<String> includeTables = strategyConfig.getInclude();
        boolean isInclude = includeTables.size() > 0;
        boolean isExclude = excludeTables.size() > 0;

        //所有的表信息
        final List<IntrospectedTable> tableList = new ArrayList<>();
        try (Connection connection = dataSourceConfig.getConnection()) {
            final String catalog = connection.getCatalog();

            final DatabaseMetaData dbmd = connection.getMetaData();
            // 数据库支持的表类型
            List<String> supportedTableTypes = JdbcUtils.extractSingleColumn(dbmd.getTableTypes(), String.class);

            // 获取表的元数据信息（不包含表的字段）
            ResultSet resultSet = dbmd.getTables(catalog, schemaPattern, tableNamePattern, tableTypes);
            List<TableMetadata> tableMetadataList = JdbcUtils.extractRows(resultSet, TableMetadata.class);

            //需要反向生成或排除的表信息
            List<IntrospectedTable> includeTableList = new ArrayList<>();
            List<IntrospectedTable> excludeTableList = new ArrayList<>();

            for (TableMetadata tableMetadataItem : tableMetadataList) {
                String tableName = tableMetadataItem.getTableName();
                if (null == tableName || tableName.length() == 0) {
                    continue; // 表名一般不会为空
                }
                IntrospectedTable table = new IntrospectedTable(this.context, tableMetadataItem);
                if (globalConfig.isSwagger() && StringUtils.hasText(tableMetadataItem.getRemarks())) {
                    table.setComment(tableMetadataItem.getRemarks().replace("\"", "\\\""));
                } else {
                    table.setComment(tableMetadataItem.getRemarks());
                }
                if (isInclude && includeTables.stream().anyMatch(t -> tableNameMatches(t, tableName))) {
                    includeTableList.add(table);
                } else if (isExclude && excludeTables.stream().anyMatch(t -> tableNameMatches(t, tableName))) {
                    excludeTableList.add(table);
                }
                tableList.add(table);
            }
            // 过滤表

            if (isExclude || isInclude) {

                Set<String> tableNames;
                if (isExclude) {
                    tableNames = strategyConfig.getExclude();
                } else {
                    tableNames = strategyConfig.getInclude();
                }
                Map<String, String> notExistTables = tableNames
                    .stream()
                    .filter(s -> !JdbcUtils.matcherRegTable(s)) // 过滤掉无意义的输入表名
                    .collect(Collectors.toMap(String::toLowerCase, s -> s, (o, n) -> n)); // 表名小写
                // 将已经存在的表移除，获取配置中数据库不存在的表
                if (!notExistTables.isEmpty()) {
                    for (IntrospectedTable tabInfo : tableList) {
                        //解决可能大小写不敏感的情况导致无法移除掉
                        notExistTables.remove(tabInfo.getName().toLowerCase());
                    }
                }
                if (notExistTables.size() > 0) {
                    LOGGER.warn("表[{}]在数据库中不存在！！！", String.join(",", notExistTables.values()));
                }
                // 需要反向生成的表信息
                if (isExclude) {
                    tableList.removeAll(excludeTableList);
                } else {
                    tableList.clear();
                    tableList.addAll(includeTableList);
                }
            }
            // 性能优化，只处理需执行表字段 https://github.com/baomidou/mybatis-plus/issues/219
            for (IntrospectedTable tableInfo : tableList) {
                introspecteTableColumns(dbmd, tableInfo, catalog, schemaPattern);
                tableInfo.processTable();
                tableInfo.importPackage();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return tableList;
    }

    /**
     * 表名匹配
     * @param matchTableName 匹配表名
     * @param dbTableName    数据库表名
     * @return 是否匹配
     */
    private boolean tableNameMatches(String matchTableName, String dbTableName) {
        return matchTableName.equalsIgnoreCase(dbTableName) || StringUtils.matches(matchTableName, dbTableName);
    }

    /**
     * 获取表的列信息
     * @param dbmd      数据库元数据
     * @param tableInfo 表信息
     * @param catalog   catalog
     * @param schema    schema
     */
    protected void introspecteTableColumns(DatabaseMetaData dbmd, IntrospectedTable tableInfo, String catalog, String schema) {
        String tableName = tableInfo.getName();
        // 主键信息
        final List<PrimaryKey> primaryKeys;
        try (ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, tableName)) {
            primaryKeys = JdbcUtils.extractRows(rs, PrimaryKey.class);
        } catch (SQLException e) {
            throw new RuntimeException("读取表主键信息:" + tableName + "错误:", e);
        }
        tableInfo.setPrimaryKeys(primaryKeys);

        // 列信息
        List<ColumnMetadata> columnMetadataList;
        try (ResultSet resultSet = dbmd.getColumns(catalog, schema, tableName, "%")) {
            columnMetadataList = JdbcUtils.extractRows(resultSet, ColumnMetadata.class);
        } catch (SQLException e) {
            throw new RuntimeException("读取表字段信息:" + tableName + "错误:", e);
        }

        for (ColumnMetadata columnMetadata : columnMetadataList) {
            String columnName = columnMetadata.getColumnName();
            IntrospectedColumn column = new IntrospectedColumn(tableInfo, this.context, columnMetadata);
            // 处理ID
            if (column.isPrimaryKey()) {
                column.setPrimaryKeyFlag(columnMetadata.isAutoIncrement());
                if (column.isKeyIdentityFlag()) {
                    LOGGER.warn("当前表[{}]的主键为自增主键，会导致全局主键的ID类型设置失效!", tableName);
                }
            }
            column.setColumnName(columnName);
            column.setComment(columnMetadata.getRemarks());
        }
    }
}
