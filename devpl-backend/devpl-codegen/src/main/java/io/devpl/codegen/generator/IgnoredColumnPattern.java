package io.devpl.codegen.generator;

import io.devpl.codegen.util.Messages;
import io.devpl.codegen.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IgnoredColumnPattern {

    private final String patternRegex;
    private final Pattern pattern;
    private final List<IgnoredColumnException> exceptions = new ArrayList<>();

    public IgnoredColumnPattern(String patternRegex) {
        this.patternRegex = patternRegex;
        pattern = Pattern.compile(patternRegex);
    }

    public void addException(IgnoredColumnException exception) {
        exceptions.add(exception);
    }

    public boolean matches(String columnName) {
        boolean matches = pattern.matcher(columnName).matches();
        if (matches) {
            for (IgnoredColumnException exception : exceptions) {
                if (exception.matches(columnName)) {
                    matches = false;
                    break;
                }
            }
        }
        return matches;
    }

    public void validate(List<String> errors, String tableName) {
        if (!StringUtils.hasText(patternRegex)) {
            errors.add(Messages.getString("ValidationError.27", tableName));
        }
    }
}
