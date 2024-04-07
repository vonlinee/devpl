package io.devpl.codegen.generator.config.xml;

import java.io.Serial;
import java.util.List;

public class XMLParserException extends MultiMessageException {

    @Serial
    private static final long serialVersionUID = 3481108770555387812L;

    public XMLParserException(List<String> errors) {
        super(errors);
    }

    public XMLParserException(String error) {
        super(error);
    }
}
