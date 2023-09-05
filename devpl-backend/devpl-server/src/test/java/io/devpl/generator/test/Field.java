package io.devpl.generator.test;

import lombok.Data;

@Data
public class Field<T> {

    private String fieldName;

    private T fieldValue;
}
