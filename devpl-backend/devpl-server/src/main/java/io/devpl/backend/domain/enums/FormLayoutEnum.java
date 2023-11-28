package io.devpl.backend.domain.enums;

/**
 * 表单布局 枚举
 */
public enum FormLayoutEnum {
    ONE(1),
    TWO(2);

    private final Integer value;

    FormLayoutEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
