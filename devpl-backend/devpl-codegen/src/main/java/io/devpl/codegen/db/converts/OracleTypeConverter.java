package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * Oracle 数据库生成对应实体类时字段类型转换，跟据 Oracle 中的数据类型，返回对应的 Java 类型
 */
public class OracleTypeConverter implements TypeConverter {
    public static final OracleTypeConverter INSTANCE = new OracleTypeConverter();

    /**
     * 将对应的类型名称转换为对应的 java 类类型
     * <p>
     * String.valueOf(Integer.MAX_VALUE).length() == 10
     * Integer 不一定能装下 10 位的数字
     * <p>
     * String.valueOf(Long.MAX_VALUE).length() == 19
     * Long 不一定能装下 19 位的数字
     *
     * @param typeName 类型名称
     * @return 返回列类型
     */
    private static JavaFieldDataType toNumberType(String typeName) {
        if (typeName.matches("number\\([0-9]\\)")) {
            return DbFieldDataType.INTEGER;
        } else if (typeName.matches("number\\(1[0-8]\\)")) {
            return DbFieldDataType.LONG;
        }
        return DbFieldDataType.BIG_DECIMAL;
    }

    /**
     * 当前时间为字段类型，根据全局配置返回对应的时间类型
     *
     * @param config 全局配置
     * @return 时间类型
     * @see GlobalConfiguration#getDateType()
     */
    protected static JavaFieldDataType toDateType(GlobalConfiguration config) {
        return switch (config.getDateType()) {
            case ONLY_DATE -> DbFieldDataType.DATE;
            case SQL_PACK -> DbFieldDataType.TIMESTAMP;
            case TIME_PACK -> DbFieldDataType.LOCAL_DATE_TIME;
        };
    }

    /**
     * 处理类型转换
     *
     * @param config    全局配置
     * @param fieldType 字段类型
     * @return 返回的对应的列类型
     */
    @Override
    public JavaFieldDataType convert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "clob").then(DbFieldDataType.STRING))
            .test(TypeConverts.containsAny("date", "timestamp").then(p -> toDateType(config)))
            .test(TypeConverts.contains("number").then(OracleTypeConverter::toNumberType))
            .test(TypeConverts.contains("float").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.contains("blob").then(DbFieldDataType.BLOB))
            .test(TypeConverts.containsAny("binary", "raw").then(DbFieldDataType.BYTE_ARRAY))
            .or(DbFieldDataType.STRING);
    }
}
