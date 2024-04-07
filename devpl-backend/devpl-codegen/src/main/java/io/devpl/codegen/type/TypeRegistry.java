package io.devpl.codegen.type;

import io.devpl.codegen.generator.config.DateType;
import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型注册处理类
 *
 * @author nieqiurong 2022/5/11.
 */
public class TypeRegistry {

    private static final Map<Integer, JavaFieldDataType> typeMap = new HashMap<>();

    static {
        // byte[]
        typeMap.put(Types.BINARY, DbFieldDataType.BYTE_ARRAY);
        typeMap.put(Types.BLOB, DbFieldDataType.BYTE_ARRAY);
        typeMap.put(Types.LONGVARBINARY, DbFieldDataType.BYTE_ARRAY);
        typeMap.put(Types.VARBINARY, DbFieldDataType.BYTE_ARRAY);
        // byte
        typeMap.put(Types.TINYINT, DbFieldDataType.BYTE);
        // long
        typeMap.put(Types.BIGINT, DbFieldDataType.LONG);
        // boolean
        typeMap.put(Types.BIT, DbFieldDataType.BOOLEAN);
        typeMap.put(Types.BOOLEAN, DbFieldDataType.BOOLEAN);
        // short
        typeMap.put(Types.SMALLINT, DbFieldDataType.SHORT);
        // string
        typeMap.put(Types.CHAR, DbFieldDataType.STRING);
        typeMap.put(Types.CLOB, DbFieldDataType.STRING);
        typeMap.put(Types.VARCHAR, DbFieldDataType.STRING);
        typeMap.put(Types.LONGVARCHAR, DbFieldDataType.STRING);
        typeMap.put(Types.LONGNVARCHAR, DbFieldDataType.STRING);
        typeMap.put(Types.NCHAR, DbFieldDataType.STRING);
        typeMap.put(Types.NCLOB, DbFieldDataType.STRING);
        typeMap.put(Types.NVARCHAR, DbFieldDataType.STRING);
        // date
        typeMap.put(Types.DATE, DbFieldDataType.DATE);
        // timestamp
        typeMap.put(Types.TIMESTAMP, DbFieldDataType.TIMESTAMP);
        // double
        typeMap.put(Types.FLOAT, DbFieldDataType.DOUBLE);
        typeMap.put(Types.REAL, DbFieldDataType.DOUBLE);
        // int
        typeMap.put(Types.INTEGER, DbFieldDataType.INTEGER);
        // bigDecimal
        typeMap.put(Types.NUMERIC, DbFieldDataType.BIG_DECIMAL);
        typeMap.put(Types.DECIMAL, DbFieldDataType.BIG_DECIMAL);
        // TODO 类型需要补充完整
    }

    /**
     * 根据字段元数据获取对应的Java类型
     *
     * @param jdbcType JDBC类型
     * @param length   长度
     * @param dateType 日期类型
     * @param scale    精度，某些字段没有
     * @return Java类型
     */
    public static JavaType getColumnType(Integer jdbcType, Integer length, DateType dateType, Integer scale) {
        // TODO 是否用包装类??? 可以尝试判断字段是否允许为null来判断是否用包装类
        // TODO 需要增加类型处理，尚未补充完整
        JavaType columnType;
        switch (jdbcType) {
            case Types.BIT -> {
                if (length > 1) {
                    columnType = DbFieldDataType.BYTE_ARRAY;
                } else {
                    columnType = DbFieldDataType.BOOLEAN;
                }
            }
            case Types.DATE -> columnType = switch (dateType) {
                case SQL_PACK -> DbFieldDataType.DATE_SQL;
                case TIME_PACK -> DbFieldDataType.LOCAL_DATE;
                default -> DbFieldDataType.DATE;
            };
            case Types.TIME -> {
                if (dateType == DateType.TIME_PACK) {
                    columnType = DbFieldDataType.LOCAL_TIME;
                } else {
                    columnType = DbFieldDataType.TIME;
                }
            }
            case Types.DECIMAL, Types.NUMERIC -> {
                if (scale > 0 || length > 18) {
                    columnType = typeMap.get(jdbcType);
                } else if (length > 9) {
                    columnType = DbFieldDataType.LONG;
                } else if (length > 4) {
                    columnType = DbFieldDataType.INTEGER;
                } else {
                    columnType = DbFieldDataType.SHORT;
                }
            }
            case Types.TIMESTAMP -> {
                if (dateType == DateType.TIME_PACK) {
                    columnType = DbFieldDataType.LOCAL_DATE_TIME;
                } else if (dateType == DateType.ONLY_DATE) {
                    columnType = DbFieldDataType.DATE;
                } else {
                    columnType = DbFieldDataType.TIMESTAMP;
                }
            }
            default -> columnType = typeMap.getOrDefault(jdbcType, DbFieldDataType.OBJECT);
        }
        return columnType;
    }
}
