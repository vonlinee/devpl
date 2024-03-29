package io.devpl.codegen.db.query;

import io.devpl.codegen.config.DataSourceConfig;
import io.devpl.codegen.config.GlobalConfig;
import io.devpl.codegen.config.StrategyConfig;
import io.devpl.codegen.core.ColumnGeneration;
import io.devpl.codegen.core.TableGeneration;
import io.devpl.codegen.jdbc.JdbcUtils;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.jdbc.meta.PrimaryKeyMetadata;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import io.devpl.codegen.type.TypeConverter;
import io.devpl.sdk.util.StringUtils;
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
 *
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
public class DefaultDatabaseIntrospection extends AbstractDatabaseIntrospector {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 查询需要生成的所有表信息
     *
     * @return 所有表信息
     */
    @Override
    public List<TableGeneration> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] tableTypes) {
        StrategyConfig strategyConfig = this.context.getStrategyConfig();
        DataSourceConfig dataSourceConfig = this.context.getDataSourceConfig();
        GlobalConfig globalConfig = this.context.getGlobalConfig();

        final Set<String> excludeTables = strategyConfig.getExclude();
        final Set<String> includeTables = strategyConfig.getInclude();
        boolean isInclude = !includeTables.isEmpty();
        boolean isExclude = !excludeTables.isEmpty();

        // 所有的表信息
        final List<TableGeneration> tableList = new ArrayList<>();
        try (Connection connection = dataSourceConfig.getConnection()) {
            catalog = catalog == null ? connection.getCatalog() : catalog;

            final DatabaseMetaData dbmd = connection.getMetaData();
            // 数据库支持的表类型
            List<String> supportedTableTypes = JdbcUtils.getSupportedTableTypes(dbmd);

            log.info("支持的表类型 {}", supportedTableTypes);
            // 获取表的元数据信息（不包含表的字段）
            ResultSet resultSet = dbmd.getTables(catalog, schemaPattern, tableNamePattern, tableTypes);
            List<TableMetadata> tableMetadataList = new ArrayList<>();
            while (resultSet.next()) {
                TableMetadata tmd = new TableMetadata();
                tmd.initialize(resultSet);
                tableMetadataList.add(tmd);
            }

            // 需要反向生成或排除的表信息
            List<TableGeneration> includeTableList = new ArrayList<>();
            List<TableGeneration> excludeTableList = new ArrayList<>();

            for (TableMetadata tableMetadataItem : tableMetadataList) {
                String tableName = tableMetadataItem.getTableName();
                if (null == tableName || tableName.isEmpty()) {
                    continue; // 表名一般不会为空
                }
                TableGeneration table = new TableGeneration(tableMetadataItem);
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
                    for (TableGeneration tabInfo : tableList) {
                        // 解决可能大小写不敏感的情况导致无法移除掉
                        notExistTables.remove(tabInfo.getName().toLowerCase());
                    }
                }
                if (!notExistTables.isEmpty()) {
                    log.warn("表[{}]在数据库{}中不存在！！！", String.join(",", notExistTables.values()), catalog);
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
            for (TableGeneration table : tableList) {
                introspectTableColumns(dbmd, table, catalog, schemaPattern);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return tableList;
    }

    @Override
    public List<ColumnGeneration> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) {
        return null;
    }

    /**
     * 表名匹配
     *
     * @param matchTableName 匹配表名
     * @param dbTableName    数据库表名
     * @return 是否匹配
     */
    private boolean tableNameMatches(String matchTableName, String dbTableName) {
        return matchTableName.equalsIgnoreCase(dbTableName) || StringUtils.matches(matchTableName, dbTableName);
    }

    /**
     * 获取表的列信息
     *
     * @param dbmd      数据库元数据
     * @param tableInfo 表信息
     * @param catalog   catalog
     * @param schema    schema
     */
    protected void introspectTableColumns(DatabaseMetaData dbmd, TableGeneration tableInfo, String catalog, String schema) {
        String tableName = tableInfo.getName();
        // 主键信息
        final List<PrimaryKeyMetadata> primaryKeys = new ArrayList<>();
        try (ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, tableName)) {
            while (rs.next()) {
                PrimaryKeyMetadata pkmd = new PrimaryKeyMetadata();
                pkmd.initialize(rs);
                primaryKeys.add(pkmd);
            }
        } catch (SQLException e) {
            throw new RuntimeException("读取表主键信息:" + tableName + "错误:", e);
        }
        tableInfo.setPrimaryKeys(primaryKeys);

        // 列信息
        List<ColumnMetadata> columnMetadataList = new ArrayList<>();
        try (ResultSet resultSet = dbmd.getColumns(catalog, schema, tableName, "%")) {
            while (resultSet.next()) {
                ColumnMetadata cmd = new ColumnMetadata();
                cmd.initialize(resultSet);
                columnMetadataList.add(cmd);
            }
        } catch (SQLException e) {
            throw new RuntimeException("读取表字段信息:" + tableName + "错误:", e);
        }

        for (ColumnMetadata columnMetadata : columnMetadataList) {
            ColumnGeneration column = new ColumnGeneration(tableInfo, columnMetadata);

            tableInfo.addColumn(column);

            // 处理ID
            if (column.isPrimaryKey()) {
                column.setPrimaryKeyFlag(columnMetadata.isAutoIncrement());
                if (column.isKeyIdentityFlag()) {
                    log.warn("当前表[{}]的主键为自增主键，会导致全局主键的ID类型设置失效!", tableName);
                }
            }
            column.setColumnName(columnMetadata.getColumnName());
            column.setComment(columnMetadata.getRemarks());
        }
    }
}
