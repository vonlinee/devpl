package io.devpl.backend.tools.mybatis;

import io.devpl.codegen.type.DataType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MyBatis Mapper Statement 参数值数据类型
 */
public enum MSParamDataType implements DataType {

    /**
     * 数值类型
     */
    NUMERIC(1, "Numeric") {
        @Override
        public boolean matches(String literalValue, StringBuilder error) {
            literalValue = literalValue.trim();
            // 支持带符号的小数和科学计数法，例如:
            // "123", "-123", "+123", "0", "12.34", "-12.34", "3.14e10", "2E+5",
            // "-2e-3", "0.001", "abc", "1.2.3", "12e3.4", "2.3E-4"
            // ^：表示字符串的开始。
            // [+-]?：表示可选的正号或负号。
            // \\d+：表示一个或多个数字。
            // (\\.\\d+)?：表示可选的小数部分，. 表示小数点，后面必须跟一个或多个数字
            // ([eE][+-]?\\d+)?：表示可选的科学计数法部分
            //      [eE]：表示科学计数法的 e 或 E
            //      [+-]?：表示可选的正号或负号
            //      \\d+：表示一个或多个数字
            // $：表示字符串的结束
            return "^[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?$".matches(literalValue);
        }

        @Nullable
        @Override
        public Object deserialize(String literalValue, StringBuilder sb) {
            if (literalValue == null) {
                return null;
            }
            literalValue = literalValue.trim();
            return Integer.parseInt(literalValue);
        }
    },
    /**
     * 字符串
     */
    STRING(2, "String") {
        @Override
        public String normalize(String value) {
            return String.format("'%s'", value);
        }

        @Override
        public String quote(String value) {
            if (value == null) {
                return "";
            }
            if (!value.startsWith("'")) {
                value = "'" + value;
            }
            if (!value.endsWith("'")) {
                value = value + "'";
            }
            return value;
        }
    }, DATE(3, "Date") {
        @Nullable
        @Override
        public Object deserialize(String literalValue, StringBuilder sb) {
            return literalValue;
        }

        @Override
        public String normalize(String value) {
            return String.format("'%s'", value);
        }
    }, TIME(4, "Time") {
        @Nullable
        @Override
        public Object deserialize(String literalValue, StringBuilder sb) {
            return literalValue;
        }

        @Override
        public String normalize(String value) {
            return String.format("'%s'", value);
        }
    }, TIMESTAMP(5, "Timestamp") {
        @Nullable
        @Override
        public Object deserialize(String literalValue, StringBuilder sb) {
            return literalValue;
        }

        @Override
        public String normalize(String value) {
            return String.format("'%s'", value);
        }
    }, BOOLEAN(6, "Boolean") {
        @Nullable
        @Override
        public Object deserialize(String literalValue, StringBuilder sb) {
            if (literalValue == null || literalValue.isEmpty()) {
                return null;
            }
            literalValue = literalValue.trim();
            if ("true".equals(literalValue)) {
                return true;
            }
            if ("false".equals(literalValue)) {
                return false;
            }
            return null;
        }
    },
    /**
     * 数组，未知元素类型
     */
    ARRAY(7, "Array") {
        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public String normalize(String value) {
            if (value == null) {
                return "";
            }
            return value.replaceAll(" ", ",");
        }
    },
    /**
     * 数值序列
     */
    NUMBER_ARRAY(8, "NumberArray") {
        @Override
        public Object deserialize(String literalValue, StringBuilder sb) {
            if (literalValue == null || literalValue.isEmpty()) {
                return Collections.emptyList();
            }
            List<Number> nums = new ArrayList<>();
            String[] items = literalValue.split(",");
            for (String item : items) {
                nums.add(Integer.parseInt(item));
            }
            return nums;
        }

        @Override
        public boolean isArray() {
            return true;
        }

        @NotNull
        @Override
        public DataType getComponentType() {
            return MSParamDataType.NUMERIC;
        }
    },
    /**
     * 字符串数组
     */
    STRING_ARRAY(9, "StringArray") {
        @Override
        public Object deserialize(String literalValue, StringBuilder sb) {
            if (literalValue == null) {
                return Collections.emptyList();
            }
            String[] items = literalValue.split(",");
            return Arrays.asList(items);
        }

        @Override
        public String normalize(String value) {
            return "'" + value + "'";
        }

        @Override
        public boolean isArray() {
            return true;
        }

        @NotNull
        @Override
        public DataType getComponentType() {
            return MSParamDataType.STRING;
        }
    }, COLLECTION(10, "Collection") {
        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public String normalize(String value) {
            if (value == null) {
                return "";
            }
            return value.replaceAll(" ", ",");
        }
    }, NULL(-1, "Null");

    @Getter
    private final int type;
    private final String typeName;

    MSParamDataType(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public static MSParamDataType valueOfTypeName(String typeName) {
        if (typeName == null) {
            return null;
        }
        for (MSParamDataType item : values()) {
            if (item.getQualifier().equals(typeName)) {
                return item;
            }
        }
        return null;
    }

    public static MSParamDataType valueOfType(int type, MSParamDataType defaultValue) {
        return Arrays.stream(values()).filter(i -> i.getType() == type).findFirst().orElse(defaultValue);
    }

    public static MSParamDataType fromType(Class<?> javaType) {
        if (javaType == null) {
            return MSParamDataType.STRING;
        }
        if (Number.class.isAssignableFrom(javaType)) {
            return MSParamDataType.NUMERIC;
        }
        return MSParamDataType.STRING;
    }

    @Override
    public final String getQualifier() {
        return typeName;
    }
}
