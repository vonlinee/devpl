package io.devpl.codegen.db.converts;

import io.devpl.codegen.config.GlobalConfig;
import io.devpl.codegen.config.TypeConverter;
import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.db.DbColumnType;

/**
 * DM 字段类型转换
 *
 * @author halower, hanchunlin, daiby
 * @since 2019-06-27
 */
public class DmTypeConverter implements TypeConverter {
    public static final DmTypeConverter INSTANCE = new DmTypeConverter();

    private static ColumnJavaType toNumberType(String typeName) {
        if (typeName.matches("number\\([0-9]\\)")) {
            return DbColumnType.INTEGER;
        } else if (typeName.matches("number\\(1[0-8]\\)")) {
            return DbColumnType.LONG;
        }
        return DbColumnType.BIG_DECIMAL;
    }

    /**
     * 字符数据类型: CHAR,CHARACTER,VARCHAR
     * <p>
     * 数值数据类型: NUMBER,NUMERIC,DECIMAL,DEC,MONEY,BIT,BOOL,BOOLEAN,INTEGER,INT,BIGINT,TINYINT,BYTE,SMALLINT,BINARY,
     * VARBINARY
     * <p>
     * 近似数值数据类型: FLOAT
     * <p>
     * DOUBLE, DOUBLE PRECISION,REAL
     * <p>
     * 日期时间数据类型
     * <p>
     * 多媒体数据类型: TEXT,LONGVARCHAR,CLOB,BLOB,IMAGE
     *
     * @param config    全局配置
     * @param fieldType 字段类型
     * @return 对应的数据类型
     * @inheritDoc
     */
    @Override
    public ColumnJavaType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "text").then(DbColumnType.STRING))
            .test(TypeConverts.contains("number").then(DmTypeConverter::toNumberType))
            .test(TypeConverts.containsAny("numeric", "dec", "money").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.containsAny("bit", "bool").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.contains("bigint").then(DbColumnType.BIG_INTEGER))
            .test(TypeConverts.containsAny("int", "byte").then(DbColumnType.INTEGER))
            .test(TypeConverts.contains("binary").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbColumnType.FLOAT))
            .test(TypeConverts.containsAny("double", "real").then(DbColumnType.DOUBLE))
            .test(TypeConverts.containsAny("date", "time").then(DbColumnType.DATE))
            .test(TypeConverts.contains("clob").then(DbColumnType.CLOB))
            .test(TypeConverts.contains("blob").then(DbColumnType.BLOB))
            .test(TypeConverts.contains("image").then(DbColumnType.BYTE_ARRAY))
            .or(DbColumnType.STRING);
    }
}
