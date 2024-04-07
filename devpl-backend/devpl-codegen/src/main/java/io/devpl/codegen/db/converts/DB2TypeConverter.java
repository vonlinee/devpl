package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * DB2 字段类型转换
 */
public class DB2TypeConverter implements TypeConverter {
    public static final DB2TypeConverter INSTANCE = new DB2TypeConverter();

    /**
     * @inheritDoc
     */
    @Override
    public JavaFieldDataType convert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "text", "json", "enum").then(DbFieldDataType.STRING))
            .test(TypeConverts.contains("bigint").then(DbFieldDataType.LONG))
            .test(TypeConverts.contains("smallint").then(DbFieldDataType.BASE_SHORT))
            .test(TypeConverts.contains("int").then(DbFieldDataType.INTEGER))
            .test(TypeConverts.containsAny("date", "time", "year").then(DbFieldDataType.DATE))
            .test(TypeConverts.contains("bit").then(DbFieldDataType.BOOLEAN))
            .test(TypeConverts.contains("decimal").then(DbFieldDataType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbFieldDataType.CLOB))
            .test(TypeConverts.contains("blob").then(DbFieldDataType.BLOB))
            .test(TypeConverts.contains("binary").then(DbFieldDataType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.contains("double").then(DbFieldDataType.DOUBLE))
            .or(DbFieldDataType.STRING);
    }

}
