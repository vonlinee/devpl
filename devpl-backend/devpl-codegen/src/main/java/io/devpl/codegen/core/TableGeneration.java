package io.devpl.codegen.core;

import io.devpl.codegen.jdbc.meta.PrimaryKeyMetadata;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 包含表信息和该表的字段信息
 */
@Setter
@Getter
public class TableGeneration implements GenerationUnit {

    /**
     * 包导入信息
     */
    private final Set<String> importPackages = new TreeSet<>();
    /**
     * 公共字段
     */
    private final List<ColumnGeneration> commonColumns = new ArrayList<>();
    /**
     * 表字段
     */
    private List<ColumnGeneration> columns = new ArrayList<>();
    /**
     * 是否转换
     */
    private boolean convert;
    /**
     * 表名称
     */
    private final String name;
    /**
     * 表注释
     */
    private String comment;
    /**
     * 实体名称
     */
    private String entityName;
    /**
     * mapper名称
     */
    private String mapperName;
    /**
     * xml名称
     */
    private String xmlName;
    /**
     * Schema Name
     */
    private String schemaName;
    /**
     * service名称
     */
    private String serviceName;
    /**
     * serviceImpl名称
     */
    private String serviceImplName;
    /**
     * controller名称
     */
    private String controllerName;
    /**
     * 主键
     */
    private List<PrimaryKeyMetadata> primaryKeys;
    /**
     * 是否有主键
     */
    private boolean havePrimaryKey;
    /**
     * 字段名称集
     */
    private String fieldNames;
    /**
     * 表信息
     */
    private final TableMetadata metadata;

    /**
     * 构造方法
     *
     * @param metadata 表元数据信息
     */
    public TableGeneration(TableMetadata metadata) {
        this.metadata = metadata;
        this.name = metadata.getTableName();
    }

    /**
     * 表名
     *
     * @return 表名
     */
    public String getTableName() {
        return name;
    }

    public String getEntityPath() {
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    /**
     * 添加字段
     *
     * @param column 字段
     * @since 3.5.0
     */
    public void addColumn(ColumnGeneration column) {
        this.columns.add(column);
    }

    /**
     * 转换filed实体为 xml mapper 中的 base column 字符串信息
     */
    public String getFieldNames() {
        if (fieldNames == null || fieldNames.isBlank()) {
            this.fieldNames = this.columns.stream().map(ColumnGeneration::getColumnName).collect(Collectors.joining(", "));
        }
        return this.fieldNames;
    }

    public TableGeneration setConvert(boolean convert) {
        this.convert = convert;
        return this;
    }

    public TableGeneration setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * 是否有主键
     *
     * @return 是否有主键
     */
    public boolean hasPrimaryKey() {
        return this.primaryKeys != null && !this.primaryKeys.isEmpty();
    }
}
