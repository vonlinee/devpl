package io.devpl.codegen.db.converts;

import io.devpl.codegen.config.GlobalConfig;
import io.devpl.codegen.config.ITypeConvert;
import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.config.DateType;
import io.devpl.codegen.db.DbColumnType;

/**
 * KingbaseES 字段类型转换
 *
 * @author kingbase, hanchunlin
 * @since 2019-10-12
 */
public class OscarTypeConvert implements ITypeConvert {
    public static final OscarTypeConvert INSTANCE = new OscarTypeConvert();

    /**
     * @param globalConfig 全局配置
     * @param fieldType    字段类型
     * @return 返回对应的字段类型
     */
    @Override
    public ColumnJavaType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("CHARACTER", "char", "varchar", "text", "character varying").then(DbColumnType.STRING))
            .test(TypeConverts.containsAny("bigint", "int8").then(DbColumnType.LONG))
            .test(TypeConverts.containsAny("int", "int1", "int2", "int3", "int4", "tinyint", "integer").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("date", "time", "timestamp").then(p -> toDateType(globalConfig, p)))
            .test(TypeConverts.containsAny("bit", "boolean").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.containsAny("decimal", "numeric", "number").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbColumnType.CLOB))
            .test(TypeConverts.contains("blob").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbColumnType.FLOAT))
            .test(TypeConverts.containsAny("double", "real", "float4", "float8").then(DbColumnType.DOUBLE))
            .or(DbColumnType.STRING);
    }

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    private ColumnJavaType toDateType(GlobalConfig config, String type) {
        DateType dateType = config.getDateType();
        if (dateType == DateType.SQL_PACK) {
            return switch (type) {
                case "date" -> DbColumnType.DATE_SQL;
                case "time" -> DbColumnType.TIME;
                default -> DbColumnType.TIMESTAMP;
            };
        } else if (dateType == DateType.TIME_PACK) {
            return switch (type) {
                case "date" -> DbColumnType.LOCAL_DATE;
                case "time" -> DbColumnType.LOCAL_TIME;
                default -> DbColumnType.LOCAL_DATE_TIME;
            };
        }
        return DbColumnType.DATE;
    }

}
