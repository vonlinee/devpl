package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * PostgreSQL 字段类型转换
 */
public class PostgreSqlTypeConverter implements TypeConverter {
    public static final PostgreSqlTypeConverter INSTANCE = new PostgreSqlTypeConverter();

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static JavaFieldDataType toDateType(GlobalConfiguration config, String type) {
        return switch (config.getDateType()) {
            case SQL_PACK -> switch (type) {
                case "date" -> DbFieldDataType.DATE_SQL;
                case "time" -> DbFieldDataType.TIME;
                default -> DbFieldDataType.TIMESTAMP;
            };
            case TIME_PACK -> switch (type) {
                case "date" -> DbFieldDataType.LOCAL_DATE;
                case "time" -> DbFieldDataType.LOCAL_TIME;
                default -> DbFieldDataType.LOCAL_DATE_TIME;
            };
            default -> DbFieldDataType.DATE;
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public JavaFieldDataType convert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "text", "json", "enum").then(DbFieldDataType.STRING))
            .test(TypeConverts.contains("bigint").then(DbFieldDataType.LONG))
            .test(TypeConverts.contains("int").then(DbFieldDataType.INTEGER))
            .test(TypeConverts.containsAny("date", "time").then(t -> toDateType(config, t)))
            .test(TypeConverts.contains("bit").then(DbFieldDataType.BOOLEAN))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbFieldDataType.BIG_DECIMAL))
            .test(TypeConverts.contains("bytea").then(DbFieldDataType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.contains("double").then(DbFieldDataType.DOUBLE))
            .test(TypeConverts.contains("boolean").then(DbFieldDataType.BOOLEAN))
            .or(DbFieldDataType.STRING);
    }
}
