package io.devpl.backend.tools.ddl;

import java.util.concurrent.ConcurrentHashMap;

public class SqlTypeMapUtil {

    private static volatile SqlTypeMapUtil sqlTypeMapUtil;

    private SqlTypeMapUtil() {
    }

    public static SqlTypeMapUtil getInstance() {
        if (sqlTypeMapUtil == null) {
            synchronized (SqlTypeMapUtil.class) {
                if (sqlTypeMapUtil == null) {
                    sqlTypeMapUtil = new SqlTypeMapUtil();
                }
            }
        }
        return sqlTypeMapUtil;
    }

    public ConcurrentHashMap<String, SqlTypeInfo> convertMapInit() {
        MySettingProperties properties = MainSetting.getInstance().myProperties;
        ConcurrentHashMap<String, SqlTypeInfo> convertMap = new ConcurrentHashMap<>();
        convertMap.put("int", new SqlTypeInfo(properties.getIntType(), properties.getIntDefaultLength()));
        convertMap.put("Integer", new SqlTypeInfo(properties.getIntType(), properties.getIntDefaultLength()));
        convertMap.put("long", new SqlTypeInfo(properties.getLongType(), properties.getLongDefaultLength()));
        convertMap.put("Long", new SqlTypeInfo(properties.getLongType(), properties.getLongDefaultLength()));
        convertMap.put("double", new SqlTypeInfo(properties.getDoubleType(), properties.getDoubleDefaultLength()));
        convertMap.put("Double", new SqlTypeInfo(properties.getDoubleType(), properties.getDoubleDefaultLength()));
        convertMap.put("float", new SqlTypeInfo(properties.getFloatType(), properties.getFloatDefaultLength()));
        convertMap.put("Float", new SqlTypeInfo(properties.getFloatType(), properties.getFloatDefaultLength()));
        convertMap.put("boolean", new SqlTypeInfo(properties.getBooleanType(), properties.getBooleanDefaultLength()));
        convertMap.put("Boolean", new SqlTypeInfo(properties.getBooleanType(), properties.getBooleanDefaultLength()));
        convertMap.put("Date", new SqlTypeInfo(properties.getDateType(), properties.getDateDefaultLength()));
        convertMap.put("String", new SqlTypeInfo(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("char", new SqlTypeInfo(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("Character", new SqlTypeInfo(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("BigDecimal", new SqlTypeInfo(properties.getBigDecimalType(), properties.getBigDecimalDefaultLength()));
        convertMap.put("LocalDate", new SqlTypeInfo(properties.getLocalDateType(), properties.getLocalDateDefaultLength()));
        convertMap.put("LocalTime", new SqlTypeInfo(properties.getLocalTimeType(), properties.getLocalTimeDefaultLength()));
        convertMap.put("LocalDateTime", new SqlTypeInfo(properties.getLocalDateTimeType(), properties.getLocalDateTimeDefaultLength()));
        return convertMap;
    }

    public SqlTypeInfo typeConvert(String javaType) {
        return null;
    }
}
