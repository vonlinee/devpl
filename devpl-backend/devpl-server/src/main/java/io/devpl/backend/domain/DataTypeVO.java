package io.devpl.backend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTypeVO {

    private String name;

    private String value;

    private String label;

    public DataTypeVO(String text) {
        this.name = text;
        this.value = text;
        this.label = text;
    }

    public DataTypeVO(String name, String value, String label) {
        this.name = name;
        this.value = value;
        this.label = label;
    }
}
