package io.devpl.codegen.type;

public interface DataTypeHandler {

    String toString(Object value);

    Object fromString(String literalValue);
}
