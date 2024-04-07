package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.DateType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * KingbaseES 字段类型转换
 */
public class OscarTypeConverter implements TypeConverter {
    public static final OscarTypeConverter INSTANCE = new OscarTypeConverter();

    /**
     * @param globalConfiguration 全局配置
     * @param fieldType           字段类型
     * @return 返回对应的字段类型
     */
    @Override
    public JavaFieldDataType convert(GlobalConfiguration globalConfiguration, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("CHARACTER", "char", "varchar", "text", "character varying").then(DbFieldDataType.STRING))
            .test(TypeConverts.containsAny("bigint", "int8").then(DbFieldDataType.LONG))
            .test(TypeConverts.containsAny("int", "int1", "int2", "int3", "int4", "tinyint", "integer").then(DbFieldDataType.INTEGER))
            .test(TypeConverts.containsAny("date", "time", "timestamp").then(p -> toDateType(globalConfiguration, p)))
            .test(TypeConverts.containsAny("bit", "boolean").then(DbFieldDataType.BOOLEAN))
            .test(TypeConverts.containsAny("decimal", "numeric", "number").then(DbFieldDataType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbFieldDataType.CLOB))
            .test(TypeConverts.contains("blob").then(DbFieldDataType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.containsAny("double", "real", "float4", "float8").then(DbFieldDataType.DOUBLE))
            .or(DbFieldDataType.STRING);
    }

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    private JavaFieldDataType toDateType(GlobalConfiguration config, String type) {
        DateType dateType = config.getDateType();
        if (dateType == DateType.SQL_PACK) {
            return switch (type) {
                case "date" -> DbFieldDataType.DATE_SQL;
                case "time" -> DbFieldDataType.TIME;
                default -> DbFieldDataType.TIMESTAMP;
            };
        } else if (dateType == DateType.TIME_PACK) {
            return switch (type) {
                case "date" -> DbFieldDataType.LOCAL_DATE;
                case "time" -> DbFieldDataType.LOCAL_TIME;
                default -> DbFieldDataType.LOCAL_DATE_TIME;
            };
        }
        return DbFieldDataType.DATE;
    }

}
