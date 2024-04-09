package io.devpl.codegen.generator.config.xml;

import java.util.List;

import io.devpl.codegen.util.Messages;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ParserErrorHandler implements ErrorHandler {

    private final List<String> warnings;

    private final List<String> errors;

    public ParserErrorHandler(List<String> warnings, List<String> errors) {
        super();
        this.warnings = warnings;
        this.errors = errors;
    }

    @Override
    public void warning(SAXParseException exception) {
        warnings.add(Messages.getString("Warning.7",
            Integer.toString(exception.getLineNumber()), exception
                .getMessage()));
    }

    @Override
    public void error(SAXParseException exception) {
        errors.add(Messages.getString("RuntimeError.4",
            Integer.toString(exception.getLineNumber()), exception
                .getMessage()));
    }

    @Override
    public void fatalError(SAXParseException exception) {
        errors.add(Messages.getString("RuntimeError.4",
            Integer.toString(exception.getLineNumber()), exception
                .getMessage()));
    }
}
