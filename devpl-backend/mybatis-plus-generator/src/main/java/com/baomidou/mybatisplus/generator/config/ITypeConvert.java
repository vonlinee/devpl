package com.baomidou.mybatisplus.generator.config;

import com.baomidou.mybatisplus.generator.config.rules.ColumnJavaType;

/**
 * 数据库字段类型转换
 *
 * @author hubin
 * @author hanchunlin
 * @since 2017-01-20
 */
public interface ITypeConvert {

    /**
     * 执行类型转换
     *
     * @param globalConfig 全局配置
     * @param fieldType    字段类型
     * @return ignore
     */
    ColumnJavaType processTypeConvert(GlobalConfig globalConfig, String fieldType);
}
