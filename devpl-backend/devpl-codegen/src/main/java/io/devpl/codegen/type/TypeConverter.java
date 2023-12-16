package io.devpl.codegen.type;

import io.devpl.codegen.config.GlobalConfig;
import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;

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
