package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.db.DbColumnType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

import static io.devpl.codegen.db.DbColumnType.*;

/**
 * SQLServer 字段类型转换
 */
public class SqlServerTypeConverter implements TypeConverter {

    public static final SqlServerTypeConverter INSTANCE = new SqlServerTypeConverter();

    /**
     * 转换为日期类型
     *
     * @param config    配置信息
     * @param fieldType 类型
     * @return 返回对应的列类型
     */
    public static ColumnJavaType toDateType(GlobalConfiguration config, String fieldType) {
        return switch (config.getDateType()) {
            case SQL_PACK -> switch (fieldType) {
                case "date" -> DATE_SQL;
                case "time" -> TIME;
                default -> TIMESTAMP;
            };
            case TIME_PACK -> switch (fieldType) {
                case "date" -> LOCAL_DATE;
                case "time" -> LOCAL_TIME;
                default -> LOCAL_DATE_TIME;
            };
            default -> DATE;
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public ColumnJavaType processTypeConvert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "xml", "text").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("date", "time").then(t -> toDateType(config, t)))
            .test(TypeConverts.contains("bit").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbColumnType.DOUBLE))
            .test(TypeConverts.contains("money").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.containsAny("binary", "image").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.containsAny("float", "real").then(DbColumnType.FLOAT))
            .or(DbColumnType.STRING);
    }
}
