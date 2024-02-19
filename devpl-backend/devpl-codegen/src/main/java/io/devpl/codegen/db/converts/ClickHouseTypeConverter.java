package io.devpl.codegen.db.converts;

import io.devpl.codegen.config.GlobalConfig;
import io.devpl.codegen.config.TypeConverter;
import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.db.DbColumnType;

/**
 * ClickHouse 字段类型转换
 *
 * @author urzeye
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
    public static ColumnJavaType toDateType(GlobalConfig config, String type) {
        return switch (config.getDateType()) {
            case SQL_PACK -> {
                if ("date".equals(type)) {
                    yield DbColumnType.DATE_SQL;
                }
                yield DbColumnType.TIMESTAMP;
            }
            case TIME_PACK -> {
                if ("date".equals(type)) {
                    yield DbColumnType.LOCAL_DATE;
                }
                yield DbColumnType.LOCAL_DATE_TIME;
            }
            default -> DbColumnType.DATE;
        };
    }

    @Override
    public ColumnJavaType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny(INTEGER_TYPE).then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny(BIGINTEGER_TYPE).then(DbColumnType.BIG_INTEGER))
            .test(TypeConverts.containsAny(BIGDECIMAL_TYPE).then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.containsAny(LONG_TYPE).then(DbColumnType.LONG))
            .test(TypeConverts.contains("float32").then(DbColumnType.FLOAT))
            .test(TypeConverts.contains("float64").then(DbColumnType.DOUBLE))
            .test(TypeConverts.contains("map").then(DbColumnType.MAP))
            .test(TypeConverts.contains("array").then(DbColumnType.OBJECT))
            .test(TypeConverts.containsAny("date", "datetime", "datetime64").then(t -> toDateType(globalConfig, fieldType)))
            .test(TypeConverts.containsAny(STRING_TYPE).then(DbColumnType.STRING))
            .or(DbColumnType.STRING);
    }
}
