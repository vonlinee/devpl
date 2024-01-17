package io.devpl.backend.domain.enums;

import io.devpl.backend.common.ValueEnum;
import lombok.Getter;

@Getter
public enum FormType implements ValueEnum<String, String> {

    TEXT("text");

    private final String text;

    FormType(String text) {
        this.text = text;
    }

    @Override
    public String getKey() {
        return text;
    }

    @Override
    public String getValue() {
        return text;
    }
}
