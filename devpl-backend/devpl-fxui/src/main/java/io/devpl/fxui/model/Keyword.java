package io.devpl.fxui.model;

/**
 * The Java modifier keywords.
 */
public enum Keyword {

    DEFAULT("default"),
    PUBLIC("public"),
    PROTECTED("protected"),
    PRIVATE("private"),
    ABSTRACT("abstract"),
    STATIC("static"),
    FINAL("final"),
    TRANSIENT("transient"),
    VOLATILE("volatile"),
    SYNCHRONIZED("synchronized"),
    NATIVE("native"),
    STRICTFP("strictfp"),
    TRANSITIVE("transitive");

    private final String codeRepresentation;

    Keyword(String codeRepresentation) {
        this.codeRepresentation = codeRepresentation;
    }

    /**
     * @return the Java keyword represented by this enum constant.
     */
    public String asString() {
        return codeRepresentation;
    }
}
