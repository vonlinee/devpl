package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * SQLite 字段类型转换
 */
public class SqliteTypeConverter implements TypeConverter {
    public static final SqliteTypeConverter INSTANCE = new SqliteTypeConverter();

    /**
     * @inheritDoc
     * @see MySqlTypeConverter#toDateType(GlobalConfiguration, String)
     */
    @Override
    public JavaFieldDataType convert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.contains("bigint").then(DbFieldDataType.LONG))
            .test(TypeConverts.containsAny("tinyint(1)", "boolean").then(DbFieldDataType.BOOLEAN))
            .test(TypeConverts.contains("int").then(DbFieldDataType.INTEGER))
            .test(TypeConverts.containsAny("text", "char", "enum").then(DbFieldDataType.STRING))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbFieldDataType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbFieldDataType.CLOB))
            .test(TypeConverts.contains("blob").then(DbFieldDataType.BLOB))
            .test(TypeConverts.contains("float").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.contains("double").then(DbFieldDataType.DOUBLE))
            .test(TypeConverts.containsAny("date", "time", "year").then(t -> MySqlTypeConverter.toDateType(config, t)))
            .or(DbFieldDataType.STRING);
    }
}
