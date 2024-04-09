package io.devpl.codegen.generator.config.xml;

import io.devpl.codegen.util.Messages;

/**
 * Typesafe enum of different model types.
 */
public enum ModelType {
    HIERARCHICAL("hierarchical"),
    FLAT("flat"),
    CONDITIONAL("conditional");

    private final String type;

    ModelType(String type) {
        this.type = type;
    }

    public static ModelType getModelType(String type) {
        if (HIERARCHICAL.type.equalsIgnoreCase(type)) {
            return HIERARCHICAL;
        } else if (FLAT.type.equalsIgnoreCase(type)) {
            return FLAT;
        } else if (CONDITIONAL.type.equalsIgnoreCase(type)) {
            return CONDITIONAL;
        } else {
            throw new RuntimeException(Messages.getString(
                "RuntimeError.13", type));
        }
    }
}
