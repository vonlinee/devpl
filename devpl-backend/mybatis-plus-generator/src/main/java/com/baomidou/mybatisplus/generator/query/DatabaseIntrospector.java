package com.baomidou.mybatisplus.generator.query;

import com.baomidou.mybatisplus.generator.config.builder.Context;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedTable;

import java.util.List;

/**
 * @author nieqiurong 2021/1/6.
 * @since 3.5.0
 */
public interface DatabaseIntrospector {

    void setContext(Context context);

    /**
     * 获取表信息
     * @param schemaPattern    schema
     * @param tableNamePattern 表名
     * @param tableTypes       表类型
     * @return 表信息
     */
    List<IntrospectedTable> getTables(String schemaPattern, String tableNamePattern, String[] tableTypes);
}
