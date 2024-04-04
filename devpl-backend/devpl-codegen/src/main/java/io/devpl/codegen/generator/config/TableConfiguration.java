package io.devpl.codegen.generator.config;

import io.devpl.codegen.generator.TargetFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表配置信息
 */
@Data
public class TableConfiguration {

    // 基本信息
    private String catalog;
    private String schema;
    private String tableName;

    /**
     * 实体类名称
     */
    private String domainObjectName;
    private String alias;

    /**
     * Mapper名称
     */
    private String mapperName;

    /**
     * 单个表单独生成哪些文件
     *
     * @see TargetFile#name()
     */
    private List<String> targetFiles;

    private boolean delimitIdentifiers;
    private boolean insertStatementEnabled;
    private boolean selectByPrimaryKeyStatementEnabled;
    private boolean selectByExampleStatementEnabled;
    private boolean updateByPrimaryKeyStatementEnabled;
    private boolean deleteByPrimaryKeyStatementEnabled;
    private boolean deleteByExampleStatementEnabled;
    private boolean countByExampleStatementEnabled;
    private boolean updateByExampleStatementEnabled;
    private final List<ColumnOverride> columnOverrides = new ArrayList<>();
    private final Map<IgnoredColumn, Boolean> ignoredColumns = new HashMap<>();
    private String selectByPrimaryKeyQueryId;
    private String selectByExampleQueryId;
    private boolean wildcardEscapingEnabled;
    private boolean isAllColumnDelimitingEnabled;
    private String sqlProviderName;

    public boolean areAnyStatementsEnabled() {
        return selectByExampleStatementEnabled || selectByPrimaryKeyStatementEnabled || insertStatementEnabled || updateByPrimaryKeyStatementEnabled || deleteByExampleStatementEnabled || deleteByPrimaryKeyStatementEnabled || countByExampleStatementEnabled || updateByExampleStatementEnabled;
    }
}
