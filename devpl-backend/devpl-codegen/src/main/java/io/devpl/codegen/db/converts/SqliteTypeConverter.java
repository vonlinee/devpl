package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.db.DbColumnType;
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
    public ColumnJavaType processTypeConvert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.containsAny("tinyint(1)", "boolean").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("text", "char", "enum").then(DbColumnType.STRING))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbColumnType.CLOB))
            .test(TypeConverts.contains("blob").then(DbColumnType.BLOB))
            .test(TypeConverts.contains("float").then(DbColumnType.FLOAT))
            .test(TypeConverts.contains("double").then(DbColumnType.DOUBLE))
            .test(TypeConverts.containsAny("date", "time", "year").then(t -> MySqlTypeConverter.toDateType(config, t)))
            .or(DbColumnType.STRING);
    }
}
