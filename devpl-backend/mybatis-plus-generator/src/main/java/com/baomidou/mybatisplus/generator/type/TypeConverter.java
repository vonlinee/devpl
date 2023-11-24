package com.baomidou.mybatisplus.generator.type;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.rules.ColumnJavaType;
import com.baomidou.mybatisplus.generator.jdbc.meta.ColumnMetadata;

/**
 * 类型转换处理器
 *
 * @author nieqiurong 2022/5/12.
 * @since 3.5.3
 */
public interface TypeConverter {

    /**
     * 转换字段类型
     *
     * @param globalConfig 全局配置
     * @param metaInfo     字段元数据信息
     * @return 子类类型
     */
    ColumnJavaType convert(GlobalConfig globalConfig, ColumnMetadata metaInfo);
}
