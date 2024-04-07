package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * MYSQL 数据库字段类型转换
 * bit类型数据转换 bit(1) -> Boolean类型  bit(2->64)  -> Byte类型
 */
public class MySqlTypeConverter implements TypeConverter {
    public static final MySqlTypeConverter INSTANCE = new MySqlTypeConverter();

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static JavaFieldDataType toDateType(GlobalConfiguration config, String type) {
        String dateType = type.replaceAll("\\(\\d+\\)", "");
        return switch (config.getDateType()) {
            case ONLY_DATE -> DbFieldDataType.DATE;
            case SQL_PACK -> switch (dateType) {
                case "date", "year" -> DbFieldDataType.DATE_SQL;
                case "time" -> DbFieldDataType.TIME;
                default -> DbFieldDataType.TIMESTAMP;
            };
            case TIME_PACK -> switch (dateType) {
                case "date" -> DbFieldDataType.LOCAL_DATE;
                case "time" -> DbFieldDataType.LOCAL_TIME;
                case "year" -> DbFieldDataType.YEAR;
                default -> DbFieldDataType.LOCAL_DATE_TIME;
            };
        };
    }

    @Override
    public JavaFieldDataType convert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "text", "json", "enum").then(DbFieldDataType.STRING))
            .test(TypeConverts.contains("bigint").then(DbFieldDataType.LONG))
            .test(TypeConverts.containsAny("tinyint(1)", "bit(1)").then(DbFieldDataType.BOOLEAN))
            .test(TypeConverts.contains("bit").then(DbFieldDataType.BYTE))
            .test(TypeConverts.contains("int").then(DbFieldDataType.INTEGER))
            .test(TypeConverts.contains("decimal").then(DbFieldDataType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbFieldDataType.CLOB))
            .test(TypeConverts.contains("blob").then(DbFieldDataType.BLOB))
            .test(TypeConverts.contains("binary").then(DbFieldDataType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.contains("double").then(DbFieldDataType.DOUBLE))
            .test(TypeConverts.containsAny("date", "time", "year").then(t -> toDateType(config, t)))
            .or(DbFieldDataType.STRING);
    }
}
