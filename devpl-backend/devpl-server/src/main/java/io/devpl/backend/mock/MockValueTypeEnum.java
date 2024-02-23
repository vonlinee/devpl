package io.devpl.backend.mock;

import io.devpl.codegen.type.DataType;

/**
 * 值类型
 */
public enum MockValueTypeEnum implements DataType {

    NUMERIC,
    NUMBER,
    STRING,
    DOMAIN_STRING,
    SEQUENCE,
    FIXED,
    DATE
}
