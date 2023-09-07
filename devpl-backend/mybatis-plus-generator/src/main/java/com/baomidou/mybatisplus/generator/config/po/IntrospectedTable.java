package com.baomidou.mybatisplus.generator.config.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.Context;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.jdbc.meta.PrimaryKey;
import com.baomidou.mybatisplus.generator.jdbc.meta.TableMetadata;
import com.baomidou.mybatisplus.generator.type.JavaType;
import com.baomidou.mybatisplus.generator.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 包含表信息和该表的字段信息
 */
public class IntrospectedTable {

    /**
     * 策略配置
     */
    private final StrategyConfig strategyConfig;

    /**
     * 包导入信息
     */
    private final Set<String> importPackages = new TreeSet<>();
    /**
     * 公共字段
     */
    private final List<IntrospectedColumn> commonFields = new ArrayList<>();
    /**
     * 实体
     */
    private final Entity entity;
    /**
     * 是否转换
     */
    private boolean convert;
    /**
     * 表名称
     */
    private String name;
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
     * 表字段
     */
    private List<IntrospectedColumn> columns = new ArrayList<>();
    /**
     * 主键
     */
    private List<PrimaryKey> primaryKeys;
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
    private TableMetadata metadata;

    /**
     * 构造方法
     * @param context  配置构建
     * @param metadata 表元数据信息
     * @since 3.5.0
     */
    public IntrospectedTable(Context context, TableMetadata metadata) {
        this.strategyConfig = context.getStrategyConfig();
        this.entity = context.getStrategyConfig().entity();
        this.metadata = metadata;
        this.name = metadata.getTableName();
    }

    public List<IntrospectedColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<IntrospectedColumn> columns) {
        this.columns = columns;
    }

    public List<PrimaryKey> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<PrimaryKey> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    /**
     * 表名
     * @return 表名
     */
    public String getTableName() {
        return name;
    }

    /**
     * @since 3.5.0
     */
    protected IntrospectedTable setConvert() {
        if (strategyConfig.startsWithTablePrefix(name) || entity.isTableFieldAnnotationEnable()) {
            this.convert = true;
        } else {
            this.convert = !entityName.equalsIgnoreCase(name);
        }
        return this;
    }

    public String getEntityPath() {
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    /**
     * 添加字段
     * @param column 字段
     * @since 3.5.0
     */
    public void addColumn(IntrospectedColumn column) {
        if (entity.matchIgnoreColumns(column.getColumnName())) {
            // 忽略字段不在处理
            return;
        } else if (entity.matchSuperEntityColumns(column.getColumnName())) {
            this.commonFields.add(column);
        } else {
            this.columns.add(column);
        }
    }

    /**
     * @param importStatements 包空间
     * @return this
     * @since 3.5.0
     */
    public IntrospectedTable addImportPackages(String... importStatements) {
        return addImportPackages(Arrays.asList(importStatements));
    }

    public IntrospectedTable addImportPackages(List<String> pkgList) {
        importPackages.addAll(pkgList);
        return this;
    }

    /**
     * 转换filed实体为 xml mapper 中的 base column 字符串信息
     */
    public String getFieldNames() {
        // TODO 感觉这个也啥必要,不打算公开set方法了
        if (StringUtils.isBlank(fieldNames)) {
            this.fieldNames = this.columns.stream().map(IntrospectedColumn::getColumnName)
                .collect(Collectors.joining(", "));
        }
        return this.fieldNames;
    }

    /**
     * 导包处理
     * @since 3.5.0
     */
    public void importPackage() {
        String superEntity = entity.getSuperClass();
        if (StringUtils.hasText(superEntity)) {
            // 自定义父类
            this.importPackages.add(superEntity);
        } else {
            if (entity.isActiveRecord()) {
                // 无父类开启 AR 模式
                this.importPackages.add("com.baomidou.mybatisplus.extension.activerecord.Model");
            }
        }
        if (entity.isSerialVersionUID() || entity.isActiveRecord()) {
            this.importPackages.add("java.io.Serializable");
        }
        if (this.isConvert()) {
            this.importPackages.add("com.baomidou.mybatisplus.annotation.TableName");
        }
        IdType idType = entity.getIdType();
        if (null != idType && this.hasPrimaryKey()) {
            // 指定需要 IdType 场景
            this.importPackages.add("com.baomidou.mybatisplus.annotation.IdType");
            this.importPackages.add("com.baomidou.mybatisplus.annotation.TableId");
        }
        this.columns.forEach(field -> {
            JavaType columnType = field.getColumnType();
            if (null != columnType && null != columnType.getQualifier()) {
                importPackages.add(columnType.getQualifier());
            }
            if (field.isKeyFlag()) {
                // 主键
                if (field.isConvert() || field.isKeyIdentityFlag()) {
                    importPackages.add("com.baomidou.mybatisplus.annotation.TableId");
                }
                // 自增
                if (field.isKeyIdentityFlag()) {
                    importPackages.add("com.baomidou.mybatisplus.annotation.IdType");
                }
            } else if (field.isConvert()) {
                // 普通字段
                importPackages.add("com.baomidou.mybatisplus.annotation.TableField");
            }
            if (null != field.getFill()) {
                // 填充字段
                importPackages.add("com.baomidou.mybatisplus.annotation.TableField");
                // TODO 好像default的不用处理也行,这个做优化项目.
                importPackages.add("com.baomidou.mybatisplus.annotation.FieldFill");
            }
            if (field.isVersionField()) {
                this.importPackages.add("com.baomidou.mybatisplus.annotation.Version");
            }
            if (field.isLogicDeleteField()) {
                this.importPackages.add("com.baomidou.mybatisplus.annotation.TableLogic");
            }
        });
    }

    /**
     * 处理表信息(文件名与导包)
     * @since 3.5.0
     */
    public void processTable() {
        String entityName = entity.getNameConvert().entityNameConvert(this);
        this.setEntityName(entity.getConverterFileName().convert(entityName));
        this.mapperName = strategyConfig.mapper().getConverterMapperFileName().convert(entityName);
        this.xmlName = strategyConfig.mapper().getConverterXmlFileName().convert(entityName);
        this.serviceName = strategyConfig.service().getConverterServiceFileName().convert(entityName);
        this.serviceImplName = strategyConfig.service().getConverterServiceImplFileName().convert(entityName);
        this.controllerName = strategyConfig.controller().getConverterFileName().convert(entityName);
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public boolean isConvert() {
        return convert;
    }

    public IntrospectedTable setConvert(boolean convert) {
        this.convert = convert;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public IntrospectedTable setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName 实体名称
     * @return this
     */
    public IntrospectedTable setEntityName(String entityName) {
        this.entityName = entityName;
        // TODO 先放置在这里
        setConvert();
        return this;
    }

    public String getMapperName() {
        return mapperName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public String getControllerName() {
        return controllerName;
    }

    /**
     * 是否有主键
     * @return 是否有主键
     */
    public boolean hasPrimaryKey() {
        return this.primaryKeys != null && !this.primaryKeys.isEmpty();
    }

    /**
     * 初始化操作
     */
    public void initialize() {

    }
}
