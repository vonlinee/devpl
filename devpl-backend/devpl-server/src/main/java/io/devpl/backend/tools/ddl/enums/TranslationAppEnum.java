package io.devpl.backend.tools.ddl.enums;

import org.apache.commons.lang3.StringUtils;

public enum TranslationAppEnum {

    EMPTY("请选择翻译组件", ""),
    TENCENT("腾讯翻译", "tencent"),
    BAIDU("百度翻译", "baidu");

    private final String name;

    private final String value;

    TranslationAppEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static TranslationAppEnum findByValue(String value) {
        for (TranslationAppEnum translationAppEnum : values()) {
            if (StringUtils.equals(value, translationAppEnum.getValue())) {
                return translationAppEnum;
            }
        }
        return EMPTY;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
