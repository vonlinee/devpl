package io.devpl.codegen.lang;

public enum FieldType {

    PRIMITIVE_INT("int", int.class),
    PRIMITIVE_FLOAT("float", float.class),
    PRIMITIVE_DOUBLE("double", double.class),
    PRIMITIVE_BYTE("byte", byte.class),
    PRIMITIVE_SHORT("short", short.class),
    INTEGER("Integer", Integer.class),
    DOUBLE("Double", Double.class),
    BYTE("Byte", Byte.class);

    final String name;
    final Class<?> typeClass;

    FieldType(String name, Class<?> typeClass) {
        this.name = name;
        this.typeClass = typeClass;
    }
}
