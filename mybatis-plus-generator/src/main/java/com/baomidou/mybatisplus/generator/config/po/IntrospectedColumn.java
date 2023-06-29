/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator.config.po;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.IKeyWordsHandler;
import com.baomidou.mybatisplus.generator.config.builder.Context;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.util.StringUtils;
import com.baomidou.mybatisplus.generator.type.JavaType;
import com.baomidou.mybatisplus.generator.jdbc.meta.ColumnMetadata;
import com.baomidou.mybatisplus.generator.jdbc.meta.PrimaryKey;

import java.util.List;
import java.util.Map;

/**
 * 表字段信息
 * @author YangHu
 * @since 2016-12-03
 */
public class IntrospectedColumn {

    /**
     * 是否做注解转换，模板参数
     * 添加@TableName注解
     */
    private boolean convert;

    /**
     * 是否主键
     */
    private boolean keyFlag;

    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;

    /**
     * 字段名称
     */
    private final String name;

    /**
     * 属性名称
     */
    private String propertyName;

    private ColumnMetadata metadata;

    /**
     * 该字段属于哪个表
     */
    private final IntrospectedTable belongTable;

    /**
     * 字段类型
     */
    private JavaType columnType;

    /**
     * 字段注释
     */
    private String comment;

    /**
     * 填充
     */
    private String fill;

    /**
     * 是否关键字
     * @since 3.3.2
     */
    private boolean keyWords;

    /**
     * 数据库字段（关键字含转义符号）
     * @since 3.3.2
     */
    private String columnName;

    /**
     * 自定义查询字段列表
     */
    private Map<String, Object> customMap;

    /**
     * 实体属性配置
     */
    private final Entity entity;

    /**
     * 数据库配置
     */
    private final DataSourceConfig dataSourceConfig;

    /**
     * 全局配置
     */
    private final GlobalConfig globalConfig;

    /**
     * 构造方法
     * @param configBuilder 配置构建
     * @param metadata      数据库字段元数据
     * @since 3.5.0
     */
    public IntrospectedColumn(IntrospectedTable table, Context configBuilder, ColumnMetadata metadata) {
        this.metadata = metadata;
        this.name = metadata.getColumnName();
        this.columnName = name;
        this.belongTable = table;
        table.addColumn(this);
        this.entity = configBuilder.getStrategyConfig().entity();
        this.dataSourceConfig = configBuilder.getDataSourceConfig();
        this.globalConfig = configBuilder.getGlobalConfig();
    }

    /**
     * @param columnType 字段类型
     */
    public void setColumnType(JavaType columnType) {
        this.columnType = columnType;
    }

    /**
     * 设置属性名称
     * @param propertyName 属性名
     * @return this
     * @since 3.5.0
     */
    public IntrospectedColumn setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    /**
     * Java字段类型名称
     * @return 该列的Java字段类型名称
     */
    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getName();
        }
        return null;
    }

    /**
     * 按 JavaBean 规则来生成 get 和 set 方法后面的属性名称
     * 需要处理一下特殊情况：
     * <p>
     * 1、如果只有一位，转换为大写形式
     * 2、如果多于 1 位，只有在第二位是小写的情况下，才会把第一位转为小写
     * <p>
     * 我们并不建议在数据库对应的对象中使用基本类型，因此这里不会考虑基本类型的情况
     */
    public String getCapitalName() {
        if (propertyName.length() == 1) {
            return propertyName.toUpperCase();
        }
        if (Character.isLowerCase(propertyName.charAt(1))) {
            return Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        }
        return propertyName;
    }

    /**
     * 获取注解字段名称
     * @return 字段
     * @since 3.3.2
     */
    public String getAnnotationColumnName() {
        if (keyWords) {
            if (columnName.startsWith("\"")) {
                return String.format("\\\"%s\\\"", name);
            }
        }
        return columnName;
    }

    /**
     * 是否为乐观锁字段
     * @return 是否为乐观锁字段
     * @since 3.5.0
     */
    public boolean isVersionField() {
        String propertyName = entity.getVersionPropertyName();
        String columnName = entity.getVersionColumnName();
        return StringUtils.hasText(propertyName) && this.propertyName.equals(propertyName) || StringUtils.hasText(columnName) && this.name.equalsIgnoreCase(columnName);
    }

    /**
     * 是否为逻辑删除字段
     * @return 是否为逻辑删除字段
     * @since 3.5.0
     */
    public boolean isLogicDeleteField() {
        String propertyName = entity.getLogicDeletePropertyName();
        String columnName = entity.getLogicDeleteColumnName();
        return StringUtils.hasText(propertyName) && this.propertyName.equals(propertyName) || StringUtils.hasText(columnName) && this.name.equalsIgnoreCase(columnName);
    }

    /**
     * 设置主键
     * @param autoIncrement 自增标识
     * @return this
     * @since 3.5.0
     */
    public IntrospectedColumn setPrimaryKeyFlag(boolean autoIncrement) {
        this.keyFlag = true;
        this.keyIdentityFlag = autoIncrement;
        return this;
    }

    public IntrospectedColumn setComment(String comment) {
        //TODO 暂时挪动到这
        this.comment = this.globalConfig.isSwagger() && StringUtils.hasText(comment) ? comment.replace("\"", "\\\"") : comment;
        return this;
    }

    public IntrospectedColumn setColumnName(String columnName) {
        this.columnName = columnName;
        IKeyWordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
        if (keyWordsHandler != null && keyWordsHandler.isKeyWords(columnName)) {
            this.keyWords = true;
            this.columnName = keyWordsHandler.formatColumn(columnName);
        }
        return this;
    }

    public IntrospectedColumn setCustomMap(Map<String, Object> customMap) {
        this.customMap = customMap;
        return this;
    }

    public boolean isConvert() {
        return convert;
    }

    public boolean isKeyFlag() {
        return keyFlag;
    }

    public boolean isKeyIdentityFlag() {
        return keyIdentityFlag;
    }

    public String getName() {
        return name;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public JavaType getColumnType() {
        return columnType;
    }

    public String getComment() {
        return comment;
    }

    public String getFill() {
        if (StringUtils.isBlank(fill)) {
            entity.getTableFillList().stream()
                //忽略大写字段问题
                .filter(tf -> tf instanceof Column && tf.getName()
                    .equalsIgnoreCase(name) || tf instanceof Property && tf.getName().equals(propertyName)).findFirst()
                .ifPresent(tf -> this.fill = tf.getFieldFill().name());
        }
        return fill;
    }

    public boolean isKeyWords() {
        return keyWords;
    }

    public String getColumnName() {
        return columnName;
    }

    /**
     * 该列是否为主键
     * @return 该列是否为主键
     */
    public boolean isPrimaryKey() {
        List<PrimaryKey> primaryKeys = belongTable.getPrimaryKeys();
        for (PrimaryKey primaryKey : primaryKeys) {
            if (this.metadata.getColumnName().equals(primaryKey.getColumnName())) {
                return true;
            }
        }
        return false;
    }

    public ColumnMetadata getColumnMetadata() {
        return metadata;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }
}
