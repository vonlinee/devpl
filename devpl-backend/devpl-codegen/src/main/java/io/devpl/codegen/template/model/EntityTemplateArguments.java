package io.devpl.codegen.template.model;

import io.devpl.codegen.db.IdType;
import io.devpl.codegen.generator.TableGeneration;
import io.devpl.codegen.generator.config.NameConverter;
import io.devpl.codegen.generator.config.NamingStrategy;
import io.devpl.codegen.generator.config.TemplateDataModelBean;
import io.devpl.codegen.strategy.FieldFillStrategy;
import io.devpl.codegen.util.ClassUtils;
import io.devpl.sdk.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * 实体属性配置
 */
public class EntityTemplateArguments extends JavaFileTemplateArguments implements TemplateDataModelBean {

    private final static Logger log = LoggerFactory.getLogger(EntityTemplateArguments.class);
    /**
     * 自定义基础的Entity类，公共字段
     */
    private final Set<String> superEntityColumns = new HashSet<>();
    /**
     * 自定义忽略字段
     * <a href="https://github.com/baomidou/generator/issues/46">...</a>
     */
    private final Set<String> ignoreColumns = new HashSet<>();
    /**
     * 表填充字段
     */
    private final List<FieldFillStrategy> tableFillList = new ArrayList<>();
    /**
     * 名称转换
     */
    private NameConverter nameConvert;
    /**
     * 自定义继承的Entity类全称，带包名
     */
    private String superClass;
    /**
     * 实体是否生成 serialVersionUID
     */
    private boolean generateSerialVersionUID = true;

    /**
     * 【实体】是否生成字段常量（默认 false）<br>
     * -----------------------------------<br>
     * public static final String ID = "test_id";
     */
    private boolean columnConstant;

    /**
     * 【实体】是否为链式模型（默认 false）
     */
    private boolean chain;

    /**
     * 【实体】是否为lombok模型（默认 false）<br>
     * <a href="https://projectlombok.org/">document</a>
     */
    private boolean lombok;

    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean booleanColumnRemoveIsPrefix;

    /**
     * 是否生成实体时，生成字段注解（默认 false）
     */
    private boolean tableFieldAnnotationEnable;

    /**
     * 乐观锁字段名称(数据库字段)
     *
     * @since 3.5.0
     */
    private String versionColumnName;

    /**
     * 乐观锁属性名称(实体字段)
     */
    private String versionPropertyName;

    /**
     * 逻辑删除字段名称(数据库字段)
     *
     * @since 3.5.0
     */
    private String logicDeleteColumnName;

    /**
     * 逻辑删除属性名称(实体字段)
     *
     * @since 3.5.0
     */
    private String logicDeletePropertyName;
    /**
     * 数据库表名映射到实体的命名策略，默认下划线转驼峰命名
     */
    private NamingStrategy tableNamingStrategy = NamingStrategy.UNDERLINE_TO_CAMEL;
    /**
     * 数据库表字段映射到实体的命名策略
     * <p>未指定按照 naming 执行</p>
     */
    private NamingStrategy columnNamingStrategy = NamingStrategy.NO_CHANGE;
    /**
     * 开启 ActiveRecord 模式（默认 false）
     */
    private boolean activeRecord;
    /**
     * 指定生成的主键的ID类型
     */
    private IdType idType;
    /**
     * 转换输出文件名称
     */
    private Function<String, String> converterFileName = (entityName -> entityName);
    /**
     * 是否覆盖已有文件（默认 false）
     */
    private boolean fileOverride;

    private EntityTemplateArguments() {
    }

    /**
     * <p>
     * 父类 Class 反射属性转换为公共字段
     * </p>
     *
     * @param clazz 实体父类 Class
     */
    public void convertSuperEntityColumns(Class<?> clazz) {
//        List<Field> fields = TableInfoHelper.getAllFields(clazz);
//        this.superEntityColumns.addAll(fields.stream().map(field -> {
//            TableId tableId = field.getAnnotation(TableId.class);
//            if (tableId != null && StringUtils.hasText(tableId.value())) {
//                return tableId.value();
//            }
//            TableField tableField = field.getAnnotation(TableField.class);
//            if (tableField != null && StringUtils.hasText(tableField.value())) {
//                return tableField.value();
//            }
//            if (null == columnNamingStrategy || columnNamingStrategy == NamingStrategy.NO_CHANGE) {
//                return field.getName();
//            }
//            return CaseFormat.camelToUnderline(field.getName());
//        }).collect(Collectors.toSet()));
    }

    /**
     * 未指定以 naming 策略为准
     *
     * @return NamingStrategy
     */
    public NamingStrategy getColumnNamingStrategy() {
        if (columnNamingStrategy == null) {
            return tableNamingStrategy;
        }
        return columnNamingStrategy;
    }

    /**
     * 匹配父类字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     */
    public boolean matchSuperEntityColumns(String fieldName) {
        // 公共字段判断忽略大小写【 部分数据库大小写不敏感 】
        return superEntityColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    /**
     * 匹配忽略字段(忽略大小写)
     *
     * @param fieldName 字段名
     * @return 是否匹配
     */
    public boolean matchIgnoreColumns(String fieldName) {
        return ignoreColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    public NameConverter getNameConvert() {
        return nameConvert;
    }

    @Nullable
    public String getSuperClass() {
        return superClass;
    }

    public Set<String> getSuperEntityColumns() {
        return this.superEntityColumns;
    }

    public boolean isSerialVersionUID() {
        return generateSerialVersionUID;
    }

    public boolean isColumnConstant() {
        return columnConstant;
    }

    public boolean isChain() {
        return chain;
    }

    public boolean isLombok() {
        return lombok;
    }

    public boolean isTableFieldAnnotationEnable() {
        return tableFieldAnnotationEnable;
    }

    @Nullable
    public String getVersionColumnName() {
        return versionColumnName;
    }

    @Nullable
    public String getVersionPropertyName() {
        return versionPropertyName;
    }

    @Nullable
    public String getLogicDeleteColumnName() {
        return logicDeleteColumnName;
    }

    @Nullable
    public String getLogicDeletePropertyName() {
        return logicDeletePropertyName;
    }

    @NotNull
    public List<FieldFillStrategy> getTableFillList() {
        return tableFillList;
    }

    @NotNull
    public NamingStrategy getNamingStrategy() {
        return tableNamingStrategy;
    }

    public boolean isActiveRecord() {
        return activeRecord;
    }

    @Nullable
    public IdType getIdType() {
        return idType;
    }

    @NotNull
    public Function<String, String> getConverterFileName() {
        return converterFileName;
    }

    @Override
    @NotNull
    public Map<String, Object> renderData(@NotNull TableGeneration tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("idType", idType == null ? null : idType.toString());
        data.put("logicDeleteFieldName", this.logicDeleteColumnName);
        data.put("versionFieldName", this.versionColumnName);
        data.put("activeRecord", this.activeRecord);
        data.put("entitySerialVersionUID", this.generateSerialVersionUID);
        data.put("entityColumnConstant", this.columnConstant);
        data.put("entityBuilderModel", this.chain);
        data.put("chainModel", this.chain);
        data.put("entityLombokModel", this.lombok);
        data.put("entityBooleanColumnRemoveIsPrefix", this.booleanColumnRemoveIsPrefix);
        data.put("superEntityClass", ClassUtils.getSimpleName(this.superClass));
        return data;
    }

    public static class Builder {

        private final EntityTemplateArguments entity = new EntityTemplateArguments();

        public Builder() {
        }

        /**
         * 转换输出文件名称
         *
         * @param converter 　转换处理
         * @return this
         * @since 3.5.0
         */
        public Builder convertFileName(@NotNull Function<String, String> converter) {
            this.entity.converterFileName = converter;
            return this;
        }

        public EntityTemplateArguments get() {
            String superClass = this.entity.superClass;
            if (StringUtils.hasText(superClass)) {
                ClassUtils.tryLoadClass(superClass).ifPresent(this.entity::convertSuperEntityColumns);
            } else {
                if (!this.entity.superEntityColumns.isEmpty()) {
                    log.warn("Forgot to set entity supper class ?");
                }
            }
            return this.entity;
        }
    }
}
