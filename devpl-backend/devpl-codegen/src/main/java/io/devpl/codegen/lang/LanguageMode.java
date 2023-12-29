package io.devpl.codegen.lang;

import io.devpl.codegen.core.CaseFormat;

import java.lang.reflect.Modifier;

public enum LanguageMode {

    JAVA {
        /**
         * @see Modifier
         * @param modifier 访问修饰符
         * @return 对应的名称
         */
        @Override
        public String getModifierName(int modifier) {
            return switch (modifier) {
                case Modifier.ABSTRACT -> "abstract";
                case Modifier.PUBLIC -> "public";
                case Modifier.PRIVATE -> "private";
                case Modifier.PROTECTED -> "protected";
                default -> throw new IllegalStateException("Unexpected value: " + modifier);
            };
        }
    };

    /**
     * 将访问修饰符的数字代码转换成对应的名称
     *
     * @param modifier 访问修饰符
     * @return 对应的名称
     */
    public abstract String getModifierName(int modifier);

    /**
     * @param fieldName 字段名
     * @return setter方法名称
     */
    public static String getSetterMethodName(String fieldName) {
        return "set" + CaseFormat.CAPITAL_FIRST.apply(fieldName);
    }

    /**
     * @param fieldName 字段名
     * @param bool      该字段是否是布尔值
     * @return setter方法名称
     */
    public static String getGetterMethodName(String fieldName, boolean bool) {
        return (bool ? "is" : "get") + CaseFormat.CAPITAL_FIRST.apply(fieldName);
    }
}
