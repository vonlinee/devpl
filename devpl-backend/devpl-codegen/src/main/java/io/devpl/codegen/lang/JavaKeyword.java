package io.devpl.codegen.lang;

/**
 * The Java modifier keywords.
 */
public enum JavaKeyword {

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

    JavaKeyword(String codeRepresentation) {
        this.codeRepresentation = codeRepresentation;
    }

    /**
     * @return the Java keyword represented by this enum constant.
     */
    public String asString() {
        return codeRepresentation;
    }
}
