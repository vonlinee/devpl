package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * DM 字段类型转换
 */
public class DmTypeConverter implements TypeConverter {
    public static final DmTypeConverter INSTANCE = new DmTypeConverter();

    private static JavaFieldDataType toNumberType(String typeName) {
        if (typeName.matches("number\\([0-9]\\)")) {
            return DbFieldDataType.INTEGER;
        } else if (typeName.matches("number\\(1[0-8]\\)")) {
            return DbFieldDataType.LONG;
        }
        return DbFieldDataType.BIG_DECIMAL;
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
    public JavaFieldDataType convert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "text").then(DbFieldDataType.STRING))
            .test(TypeConverts.contains("number").then(DmTypeConverter::toNumberType))
            .test(TypeConverts.containsAny("numeric", "dec", "money").then(DbFieldDataType.BIG_DECIMAL))
            .test(TypeConverts.containsAny("bit", "bool").then(DbFieldDataType.BOOLEAN))
            .test(TypeConverts.contains("bigint").then(DbFieldDataType.BIG_INTEGER))
            .test(TypeConverts.containsAny("int", "byte").then(DbFieldDataType.INTEGER))
            .test(TypeConverts.contains("binary").then(DbFieldDataType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.containsAny("double", "real").then(DbFieldDataType.DOUBLE))
            .test(TypeConverts.containsAny("date", "time").then(DbFieldDataType.DATE))
            .test(TypeConverts.contains("clob").then(DbFieldDataType.CLOB))
            .test(TypeConverts.contains("blob").then(DbFieldDataType.BLOB))
            .test(TypeConverts.contains("image").then(DbFieldDataType.BYTE_ARRAY))
            .or(DbFieldDataType.STRING);
    }
}
