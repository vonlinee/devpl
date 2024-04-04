package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.*;
import io.devpl.codegen.generator.plugins.*;
import io.devpl.codegen.jdbc.JdbcDatabaseMetadataReader;
import io.devpl.codegen.jdbc.JdbcUtils;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.jdbc.meta.DatabaseMetadataReader;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import io.devpl.codegen.strategy.ProjectArchetype;
import io.devpl.codegen.strategy.SimpleMavenProjectArchetype;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import io.devpl.codegen.util.ClassUtils;
import io.devpl.codegen.util.Messages;
import io.devpl.codegen.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 配置汇总 传递给文件生成工具
 * 单个数据库实例对应一个Context
 *
 * @see org.mybatis.generator.config.Context
 */
public class RdbmsTableGenerationContext extends Context {

    private final Logger log = LoggerFactory.getLogger(RdbmsTableGenerationContext.class);

    private final List<TableConfiguration> tableConfigurations;

    /**
     * 模板路径配置信息
     */
    private final TemplateConfiguration templateConfiguration;
    /**
     * 数据库表信息
     */
    private final List<TableGeneration> targetTables = new ArrayList<>();
    /**
     * 包配置信息
     */
    private final PackageConfiguration packageConfig;
    /**
     * 数据库配置信息
     */
    private final JdbcConfiguration jdbcConfiguration;
    /**
     * 策略配置信息
     */
    private final StrategyConfiguration strategyConfiguration;
    /**
     * 全局配置信息
     */
    private GlobalConfiguration globalConfiguration;
    /**
     * 用于定位文件位置
     */
    private ProjectArchetype projectArchetype = new SimpleMavenProjectArchetype();

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig         包配置
     * @param jdbcConfiguration     数据源配置
     * @param strategyConfiguration 表配置
     * @param templateConfiguration 模板配置
     * @param globalConfiguration   全局配置
     */
    public RdbmsTableGenerationContext(PackageConfiguration packageConfig, JdbcConfiguration jdbcConfiguration, StrategyConfiguration strategyConfiguration, TemplateConfiguration templateConfiguration, GlobalConfiguration globalConfiguration) {
        this.jdbcConfiguration = jdbcConfiguration;
        this.strategyConfiguration = strategyConfiguration;
        this.globalConfiguration = globalConfiguration;
        this.templateConfiguration = templateConfiguration;
        this.packageConfig = packageConfig;

        this.tableConfigurations = new ArrayList<>();
    }

    public void addTableConfiguration(TableConfiguration tc) {
        tableConfigurations.add(tc);
    }

    /**
     * 初始化插件
     */
    @Override
    public void initialize() {
        super.initialize();
        putObject(this.jdbcConfiguration);
        putObject(this.globalConfiguration);
        putObject(this.templateConfiguration);
        putObject(this.packageConfig);
        putObject(this.strategyConfiguration);
        putObject(this.projectArchetype);

        // 为了兼容之前逻辑，默认采用 Velocity 引擎
        this.putObject(TemplateEngine.class, new VelocityTemplateEngine());

        addPlugin(new TableFileGenerationPlugin());
        addPlugin(new MyBatisPlusPlugin());
        addPlugin(new ColumnIgnorePlugin());
        addPlugin(new NamingPlugin());
        addPlugin(new TypeHandlerPlugin());
        addPlugin(new KeywordsHandlerPlugin());
        addPlugin(new ColumnCommentPlugin());
    }

    /**
     * 获取表信息
     *
     * @param tableNamePattern 表名匹配模式
     * @return 表信息
     */
    public List<TableGeneration> introspectTables(String tableNamePattern) {
        // 是否跳过视图
        boolean skipView = strategyConfiguration.isSkipView();
        // 查询的表类型
        String[] tableTypes = skipView ? new String[]{"TABLE"} : new String[]{"TABLE", "VIEW"};
        String schemaPattern = jdbcConfiguration.getSchemaName();
        DatabaseMetadataReader reader = ClassUtils.instantiate(jdbcConfiguration.getDatabaseQueryClass());
        log.info("introspect table using {}", reader);
        return introspectTables(null, schemaPattern, tableNamePattern, tableTypes);
    }

    public List<TableGeneration> introspectTables(TableConfiguration tc) {
        // 是否跳过视图
        boolean skipView = strategyConfiguration.isSkipView();
        // 查询的表类型
        String[] tableTypes = skipView ? new String[]{"TABLE"} : new String[]{"TABLE", "VIEW"};
        String schemaPattern = jdbcConfiguration.getSchemaName();
        DatabaseMetadataReader reader = ClassUtils.instantiate(jdbcConfiguration.getDatabaseQueryClass());
        log.info("introspect table using {}", reader);
        return introspectTables(null, schemaPattern, tc.getTableName(), tableTypes);
    }

    public void introspectTables(ProgressCallback callback, List<String> warnings, Set<String> fullyQualifiedTableNames) throws SQLException, InterruptedException {
        try (Connection connection = jdbcConfiguration.getConnection()) {
            for (TableConfiguration tc : tableConfigurations) {
                String tableName = StringUtils.composeFullyQualifiedTableName(tc.getCatalog(), tc.getSchema(), tc.getTableName(), '.');
                if (fullyQualifiedTableNames != null && !fullyQualifiedTableNames.isEmpty() && !fullyQualifiedTableNames.contains(tableName)) {
                    continue;
                }
                if (!tc.areAnyStatementsEnabled()) {
                    warnings.add(Messages.getString("Warning.0", tableName)); //$NON-NLS-1$
                    continue;
                }
                callback.startTask(Messages.getString("Progress.1", tableName)); //$NON-NLS-1$
                List<TableGeneration> tables = introspectTables(tc);
                if (tables != null) {
                    targetTables.addAll(tables);
                }
                callback.checkCancel();
            }
        }
    }

    /**
     * 查询需要生成的所有表信息
     *
     * @return 所有表信息
     */
    public List<TableGeneration> introspectTables(String catalog, String schemaPattern, String tableNamePattern, String[] tableTypes) {
        // 所有的表信息
        final List<TableGeneration> tableList = new ArrayList<>();
        final Set<String> excludeTables = strategyConfiguration.getExclude();
        final Set<String> includeTables = strategyConfiguration.getInclude();
        boolean isInclude = !includeTables.isEmpty();
        boolean isExclude = !excludeTables.isEmpty();

        final DatabaseMetadataReader reader = this.getObject(DatabaseMetadataReader.class, new JdbcDatabaseMetadataReader());
        try (Connection connection = jdbcConfiguration.getConnection()) {
            reader.setConnection(connection);
            catalog = catalog == null ? connection.getCatalog() : catalog;

            // 需要反向生成或排除的表信息
            List<TableGeneration> includeTableList = new ArrayList<>();
            List<TableGeneration> excludeTableList = new ArrayList<>();
            // 获取表的元数据信息（不包含表的字段）
            List<TableMetadata> tableMetadataList = reader.getTables(catalog, schemaPattern, tableNamePattern, tableTypes);
            for (TableMetadata tmd : tableMetadataList) {
                String tableName = tmd.getTableName();
                if (null == tableName || tableName.isEmpty()) {
                    continue; // 表名一般不会为空
                }
                TableGeneration table = new TableGeneration(tmd);
                if (StringUtils.hasText(tmd.getRemarks())) {
                    table.setComment(tmd.getRemarks().replace("\"", "\\\""));
                } else {
                    table.setComment(tmd.getRemarks());
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
                    tableNames = strategyConfiguration.getExclude();
                } else {
                    tableNames = strategyConfiguration.getInclude();
                }
                Map<String, String> notExistTables = tableNames.stream().filter(s -> !JdbcUtils.matcherRegTable(s)) // 过滤掉无意义的输入表名
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
            for (TableGeneration table : tableList) {
                introspectTableColumns(connection, table, catalog, schemaPattern);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return tableList;
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
     * @param tableInfo 表信息
     * @param catalog   catalog
     * @param schema    schema
     */
    protected void introspectTableColumns(Connection connection, TableGeneration tableInfo, String catalog, String schema) throws SQLException {
        String tableName = tableInfo.getName();
        JdbcDatabaseMetadataReader reader = this.getObject(JdbcDatabaseMetadataReader.class, new JdbcDatabaseMetadataReader());
        reader.setConnection(connection);
        // 主键信息
        tableInfo.setPrimaryKeys(reader.getPrimaryKeys(catalog, schema, tableName));
        // 列信息
        List<ColumnMetadata> columnMetadataList = reader.getColumns(catalog, schema, tableName, "%");

        for (ColumnMetadata columnMetadata : columnMetadataList) {
            ColumnGeneration column = new ColumnGeneration(tableInfo, columnMetadata);
            // 处理ID
            if (column.isPrimaryKey()) {
                column.setPrimaryKeyFlag(columnMetadata.isAutoIncrement());
                if (column.isKeyIdentityFlag()) {
                    log.warn("当前表[{}]的主键为自增主键，会导致全局主键的ID类型设置失效!", tableName);
                }
            }
            column.setColumnName(columnMetadata.getColumnName());
            column.setComment(columnMetadata.getRemarks());

            tableInfo.addColumn(column);
        }
    }

    /**
     * 得到要生成的文件信息
     *
     * @param files 存放结果
     */
    @Override
    public final void generateFiles(List<GeneratedFile> files) {
        // 获取表过滤
        String tableNamePattern = null;
        if (strategyConfiguration.getLikeTable() != null) {
            tableNamePattern = strategyConfiguration.getLikeTable().getValue();
        }
        // 获取所有的表信息
        List<TableGeneration> tableGenerations = this.introspectTables(tableNamePattern);
        if (!tableGenerations.isEmpty()) {
            this.targetTables.addAll(tableGenerations);
            final Plugin rootPlugin = getPlugins();
            for (TableGeneration tableGeneration : tableGenerations) {
                rootPlugin.initialize(tableGeneration);
            }
            for (TableGeneration tableGeneration : tableGenerations) {
                rootPlugin.generateFiles(tableGeneration, files);
            }
        }
    }
}
