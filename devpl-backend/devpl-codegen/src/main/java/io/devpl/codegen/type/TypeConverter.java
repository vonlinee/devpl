package io.devpl.codegen.type;

import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.db.JavaFieldDataType;
import org.apache.ddlutils.jdbc.meta.ColumnMetadata;

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
     * @param globalConfiguration 全局配置
     * @param metaInfo     字段元数据信息
     * @return 子类类型
     */
    JavaFieldDataType convert(GlobalConfiguration globalConfiguration, ColumnMetadata metaInfo);
}
