package io.devpl.codegen.db.converts;

import io.devpl.codegen.config.GlobalConfig;
import io.devpl.codegen.config.TypeConverter;
import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.db.DbColumnType;

/**
 * DB2 字段类型转换
 *
 * @author zhanyao, hanchunlin
 * @since 2018-05-16
 */
public class DB2TypeConverter implements TypeConverter {
    public static final DB2TypeConverter INSTANCE = new DB2TypeConverter();

    /**
     * @inheritDoc
     */
    @Override
    public ColumnJavaType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "text", "json", "enum").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.contains("smallint").then(DbColumnType.BASE_SHORT))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("date", "time", "year").then(DbColumnType.DATE))
            .test(TypeConverts.contains("bit").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.contains("decimal").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbColumnType.CLOB))
            .test(TypeConverts.contains("blob").then(DbColumnType.BLOB))
            .test(TypeConverts.contains("binary").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbColumnType.FLOAT))
            .test(TypeConverts.contains("double").then(DbColumnType.DOUBLE))
            .or(DbColumnType.STRING);
    }

}
