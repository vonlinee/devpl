package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * ClickHouse 字段类型转换
 */
public class ClickHouseTypeConverter implements TypeConverter {

    public static final ClickHouseTypeConverter INSTANCE = new ClickHouseTypeConverter();

    static final String[] INTEGER_TYPE = new String[]{
        "intervalyear", "intervalquarter", "intervalmonth", "intervalweek",
        "intervalday", "intervalhour", "intervalminute", "intervalsecond",
        "uint16", "uint8", "int16", "int8", "int32"
    };

    static final String[] BIGINTEGER_TYPE = new String[]{
        "uint256", "uint128", "uint64", "int256", "int128"
    };

    static final String[] BIGDECIMAL_TYPE = new String[]{
        "decimal32", "decimal64", "decimal128", "decimal256", "decimal"
    };

    static final String[] LONG_TYPE = new String[]{
        "int64", "uint32"
    };

    static final String[] STRING_TYPE = new String[]{
        "uuid", "char", "varchar", "text", "tinytext", "longtext", "blob", "tinyblob", "mediumblob", "longblob",
        "enum8", "enum16", "ipv4", "ipv6", "string", "fixedstring", "nothing", "nested", "tuple", "aggregatefunction", "unknown"
    };

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static JavaFieldDataType toDateType(GlobalConfiguration config, String type) {
        return switch (config.getDateType()) {
            case SQL_PACK -> {
                if ("date".equals(type)) {
                    yield DbFieldDataType.DATE_SQL;
                }
                yield DbFieldDataType.TIMESTAMP;
            }
            case TIME_PACK -> {
                if ("date".equals(type)) {
                    yield DbFieldDataType.LOCAL_DATE;
                }
                yield DbFieldDataType.LOCAL_DATE_TIME;
            }
            default -> DbFieldDataType.DATE;
        };
    }

    @Override
    public JavaFieldDataType convert(GlobalConfiguration globalConfiguration, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny(INTEGER_TYPE).then(DbFieldDataType.INTEGER))
            .test(TypeConverts.containsAny(BIGINTEGER_TYPE).then(DbFieldDataType.BIG_INTEGER))
            .test(TypeConverts.containsAny(BIGDECIMAL_TYPE).then(DbFieldDataType.BIG_DECIMAL))
            .test(TypeConverts.containsAny(LONG_TYPE).then(DbFieldDataType.LONG))
            .test(TypeConverts.contains("float32").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.contains("float64").then(DbFieldDataType.DOUBLE))
            .test(TypeConverts.contains("map").then(DbFieldDataType.MAP))
            .test(TypeConverts.contains("array").then(DbFieldDataType.OBJECT))
            .test(TypeConverts.containsAny("date", "datetime", "datetime64").then(t -> toDateType(globalConfiguration, fieldType)))
            .test(TypeConverts.containsAny(STRING_TYPE).then(DbFieldDataType.STRING))
            .or(DbFieldDataType.STRING);
    }
}
