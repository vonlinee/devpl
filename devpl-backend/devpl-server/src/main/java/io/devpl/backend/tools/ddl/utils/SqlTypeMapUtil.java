package io.devpl.backend.tools.ddl.utils;

import io.devpl.backend.tools.ddl.setting.MainSetting;
import io.devpl.backend.tools.ddl.setting.MySettingProperties;
import org.apache.commons.lang3.StringUtils;

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

    public ConcurrentHashMap<String, ConvertBean> convertMapInit() {
        MySettingProperties properties = MainSetting.getInstance().myProperties;
        ConcurrentHashMap<String, ConvertBean> convertMap = new ConcurrentHashMap<>();
        convertMap.put("int", new ConvertBean(properties.getIntType(), properties.getIntDefaultLength()));
        convertMap.put("Integer", new ConvertBean(properties.getIntType(), properties.getIntDefaultLength()));
        convertMap.put("long", new ConvertBean(properties.getLongType(), properties.getLongDefaultLength()));
        convertMap.put("Long", new ConvertBean(properties.getLongType(), properties.getLongDefaultLength()));
        convertMap.put("double", new ConvertBean(properties.getDoubleType(), properties.getDoubleDefaultLength()));
        convertMap.put("Double", new ConvertBean(properties.getDoubleType(), properties.getDoubleDefaultLength()));
        convertMap.put("float", new ConvertBean(properties.getFloatType(), properties.getFloatDefaultLength()));
        convertMap.put("Float", new ConvertBean(properties.getFloatType(), properties.getFloatDefaultLength()));
        convertMap.put("boolean", new ConvertBean(properties.getBooleanType(), properties.getBooleanDefaultLength()));
        convertMap.put("Boolean", new ConvertBean(properties.getBooleanType(), properties.getBooleanDefaultLength()));
        convertMap.put("Date", new ConvertBean(properties.getDateType(), properties.getDateDefaultLength()));
        convertMap.put("String", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("char", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("Character", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("BigDecimal", new ConvertBean(properties.getBigDecimalType(), properties.getBigDecimalDefaultLength()));
        convertMap.put("LocalDate", new ConvertBean(properties.getLocalDateType(), properties.getLocalDateDefaultLength()));
        convertMap.put("LocalTime", new ConvertBean(properties.getLocalTimeType(), properties.getLocalTimeDefaultLength()));
        convertMap.put("LocalDateTime", new ConvertBean(properties.getLocalDateTimeType(), properties.getLocalDateTimeDefaultLength()));
        return convertMap;
    }

    public ConvertBean typeConvert(String javaType) {
        StringUtils.isBlank(javaType);
        return null;
    }

    public static class ConvertBean {
        private String sqlType;
        private String sqlTypeLength;

        public ConvertBean(String sqlType, String sqlTypeLength) {
            this.sqlType = sqlType;
            this.sqlTypeLength = sqlTypeLength;
        }

        public String getSqlType() {
            return sqlType;
        }

        public void setSqlType(String sqlType) {
            this.sqlType = sqlType;
        }

        public String getSqlTypeLength() {
            return sqlTypeLength;
        }

        public void setSqlTypeLength(String sqlTypeLength) {
            this.sqlTypeLength = sqlTypeLength;
        }
    }
}
