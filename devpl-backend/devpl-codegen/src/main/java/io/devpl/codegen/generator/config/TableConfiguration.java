package io.devpl.codegen.generator.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.devpl.codegen.generator.file.TargetFile;
import io.devpl.codegen.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.IgnoredColumnPattern;

/**
 * 表配置信息
 *
 * @see io.devpl.codegen.generator.TableGeneration
 * @see io.devpl.codegen.generator.Context
 */
@Getter
@Setter
public class TableConfiguration extends PropertyHolder {
    /**
     * 数据库名称
     */
    private String catalog;
    /**
     * schema名称
     */
    private String schema;
    /**
     * 必须指定
     */
    private String tableName;
    /**
     * 实体类名称
     */
    private String domainObjectName;
    /**
     * 别名
     */
    private String alias;
    /**
     * Mapper名称
     */
    private String mapperName;
    /**
     * 单个表单独生成哪些文件
     *
     * @see TargetFile#getName()
     */
    private Set<String> targetFiles;

    private String configuredModelType;
    /**
     * 是否有分隔符, 表名使用指定分隔符进行分割
     */
    private boolean delimitIdentifiers;
    private boolean insertStatementEnabled;
    private boolean selectByPrimaryKeyStatementEnabled;
    private boolean selectByExampleStatementEnabled;
    private boolean updateByPrimaryKeyStatementEnabled;
    private boolean deleteByPrimaryKeyStatementEnabled;
    private boolean deleteByExampleStatementEnabled;
    private boolean countByExampleStatementEnabled;
    private boolean updateByExampleStatementEnabled;
    private List<ColumnOverride> columnOverrides = new ArrayList<>();
    private Map<IgnoredColumn, Boolean> ignoredColumns = new HashMap<>();
    private String selectByPrimaryKeyQueryId;
    private String selectByExampleQueryId;
    private boolean wildcardEscapingEnabled;
    private boolean isAllColumnDelimitingEnabled;
    private String sqlProviderName;
    private GeneratedKey generatedKey;

    /**
     * 单个表的命名策略
     */
    private NameConverter nameConverter;
    /**
     * 忽略的列模式
     */
    private final List<IgnoredColumnPattern> ignoredColumnPatterns = new ArrayList<>();

    private TableConfiguration() {
    }

    private TableConfiguration(Builder builder) {
        this.catalog = builder.catalog;
        this.schema = builder.schema;
        this.tableName = builder.tableName;
        this.domainObjectName = builder.domainObjectName;
        this.alias = builder.alias;
        this.mapperName = builder.mapperName;
        this.targetFiles = builder.targetFiles;
        this.delimitIdentifiers = builder.delimitIdentifiers;
        this.insertStatementEnabled = builder.insertStatementEnabled;
        this.selectByPrimaryKeyStatementEnabled = builder.selectByPrimaryKeyStatementEnabled;
        this.selectByExampleStatementEnabled = builder.selectByExampleStatementEnabled;
        this.updateByPrimaryKeyStatementEnabled = builder.updateByPrimaryKeyStatementEnabled;
        this.deleteByPrimaryKeyStatementEnabled = builder.deleteByPrimaryKeyStatementEnabled;
        this.deleteByExampleStatementEnabled = builder.deleteByExampleStatementEnabled;
        this.countByExampleStatementEnabled = builder.countByExampleStatementEnabled;
        this.updateByExampleStatementEnabled = builder.updateByExampleStatementEnabled;
        this.columnOverrides = builder.columnOverrides;
        this.ignoredColumns = builder.ignoredColumns;
        this.selectByPrimaryKeyQueryId = builder.selectByPrimaryKeyQueryId;
        this.selectByExampleQueryId = builder.selectByExampleQueryId;
        this.wildcardEscapingEnabled = builder.wildcardEscapingEnabled;
        this.isAllColumnDelimitingEnabled = builder.isAllColumnDelimitingEnabled;
        this.sqlProviderName = builder.sqlProviderName;
    }

    public boolean isAnyStatementsEnabled() {
        return selectByExampleStatementEnabled
            || selectByPrimaryKeyStatementEnabled
            || insertStatementEnabled
            || updateByPrimaryKeyStatementEnabled
            || deleteByExampleStatementEnabled
            || deleteByPrimaryKeyStatementEnabled
            || countByExampleStatementEnabled
            || updateByExampleStatementEnabled;
    }

    /**
     * 其他全部采用默认配置项
     *
     * @param fullQualifiedTableName 数据库名称.表名称，例如 test_db.t_user
     * @return TableConfiguration
     */
    public static TableConfiguration of(String fullQualifiedTableName) {
        if (fullQualifiedTableName.contains(".")) {
            String[] names = fullQualifiedTableName.split("\\.");
            if (names.length != 2
                || !StringUtils.isValidDatabaseIdentifier(names[0])
                || !StringUtils.isValidDatabaseIdentifier(names[1])) {
                throw new IllegalArgumentException(fullQualifiedTableName + " is not a legal identifier for table name");
            }
            return builder(names[1]).withCatalog(names[0]).build();
        }
        return builder(fullQualifiedTableName).build();
    }

    public static TableConfiguration none() {
        return new TableConfiguration();
    }

    public static Builder builder(String tableName) {
        return new Builder(tableName);
    }

    public void setConfiguredModelType(String modelType) {

    }

    public void addIgnoredColumnPattern(IgnoredColumnPattern pattern) {
        ignoredColumnPatterns.add(pattern);
    }

    public void addColumnOverride(ColumnOverride columnOverride) {
        this.columnOverrides.add(columnOverride);
    }

    public void setGeneratedKey(GeneratedKey generatedKey) {
        this.generatedKey = generatedKey;
    }

    public void addIgnoredColumn(IgnoredColumn ignoredColumn) {
        this.ignoredColumns.put(ignoredColumn, Boolean.FALSE);
    }

    public void setColumnRenamingRule(ColumnRenamingRule columnRenamingRule) {

    }

    public static final class Builder {
        private String catalog;
        private String schema;
        private String tableName;
        private String configuredModelType;
        private String domainObjectName;
        private String alias;
        private String mapperName;
        private Set<String> targetFiles = new HashSet<>();
        private boolean delimitIdentifiers;
        private boolean insertStatementEnabled;
        private boolean selectByPrimaryKeyStatementEnabled;
        private boolean selectByExampleStatementEnabled;
        private boolean updateByPrimaryKeyStatementEnabled;
        private boolean deleteByPrimaryKeyStatementEnabled;
        private boolean deleteByExampleStatementEnabled;
        private boolean countByExampleStatementEnabled;
        private boolean updateByExampleStatementEnabled;
        private List<ColumnOverride> columnOverrides = new ArrayList<>();
        private Map<IgnoredColumn, Boolean> ignoredColumns = new HashMap<>();
        private String selectByPrimaryKeyQueryId;
        private String selectByExampleQueryId;
        private boolean wildcardEscapingEnabled;
        private boolean isAllColumnDelimitingEnabled;
        private String sqlProviderName;

        private Builder(String tableName) {
            this.tableName = tableName;
        }

        public Builder withCatalog(String catalog) {
            this.catalog = catalog;
            return this;
        }

        public Builder withSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public Builder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder withDomainObjectName(String domainObjectName) {
            this.domainObjectName = domainObjectName;
            return this;
        }

        public Builder withAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder withMapperName(String mapperName) {
            this.mapperName = mapperName;
            return this;
        }

        public Builder withTargetFiles(Set<String> targetFiles) {
            this.targetFiles = targetFiles;
            return this;
        }

        public Builder withConfiguredModelType(String configuredModelType) {
            this.configuredModelType = configuredModelType;
            return this;
        }

        public Builder withDelimitIdentifiers(boolean delimitIdentifiers) {
            this.delimitIdentifiers = delimitIdentifiers;
            return this;
        }

        public Builder withInsertStatementEnabled(boolean insertStatementEnabled) {
            this.insertStatementEnabled = insertStatementEnabled;
            return this;
        }

        public Builder withSelectByPrimaryKeyStatementEnabled(boolean selectByPrimaryKeyStatementEnabled) {
            this.selectByPrimaryKeyStatementEnabled = selectByPrimaryKeyStatementEnabled;
            return this;
        }

        public Builder withSelectByExampleStatementEnabled(boolean selectByExampleStatementEnabled) {
            this.selectByExampleStatementEnabled = selectByExampleStatementEnabled;
            return this;
        }

        public Builder withUpdateByPrimaryKeyStatementEnabled(boolean updateByPrimaryKeyStatementEnabled) {
            this.updateByPrimaryKeyStatementEnabled = updateByPrimaryKeyStatementEnabled;
            return this;
        }

        public Builder withDeleteByPrimaryKeyStatementEnabled(boolean deleteByPrimaryKeyStatementEnabled) {
            this.deleteByPrimaryKeyStatementEnabled = deleteByPrimaryKeyStatementEnabled;
            return this;
        }

        public Builder withDeleteByExampleStatementEnabled(boolean deleteByExampleStatementEnabled) {
            this.deleteByExampleStatementEnabled = deleteByExampleStatementEnabled;
            return this;
        }

        public Builder withCountByExampleStatementEnabled(boolean countByExampleStatementEnabled) {
            this.countByExampleStatementEnabled = countByExampleStatementEnabled;
            return this;
        }

        public Builder withUpdateByExampleStatementEnabled(boolean updateByExampleStatementEnabled) {
            this.updateByExampleStatementEnabled = updateByExampleStatementEnabled;
            return this;
        }

        public Builder withColumnOverrides(List<ColumnOverride> columnOverrides) {
            this.columnOverrides = columnOverrides;
            return this;
        }

        public Builder withIgnoredColumns(Map<IgnoredColumn, Boolean> ignoredColumns) {
            this.ignoredColumns = ignoredColumns;
            return this;
        }

        public Builder withSelectByPrimaryKeyQueryId(String selectByPrimaryKeyQueryId) {
            this.selectByPrimaryKeyQueryId = selectByPrimaryKeyQueryId;
            return this;
        }

        public Builder withSelectByExampleQueryId(String selectByExampleQueryId) {
            this.selectByExampleQueryId = selectByExampleQueryId;
            return this;
        }

        public Builder withWildcardEscapingEnabled(boolean wildcardEscapingEnabled) {
            this.wildcardEscapingEnabled = wildcardEscapingEnabled;
            return this;
        }

        public Builder withIsAllColumnDelimitingEnabled(boolean isAllColumnDelimitingEnabled) {
            this.isAllColumnDelimitingEnabled = isAllColumnDelimitingEnabled;
            return this;
        }

        public Builder withSqlProviderName(String sqlProviderName) {
            this.sqlProviderName = sqlProviderName;
            return this;
        }

        public TableConfiguration build() {
            return new TableConfiguration(this);
        }
    }
}
