package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.IgnoredColumn;
import io.devpl.codegen.util.Messages;
import io.devpl.codegen.util.StringUtils;

import java.util.List;

public class IgnoredColumnException extends IgnoredColumn {

    public IgnoredColumnException(String columnName) {
        super(columnName);
    }

    @Override
    public void validate(List<String> errors, String tableName) {
        if (!StringUtils.hasText(columnName)) {
            errors.add(Messages.getString("ValidationError.26",
                    tableName));
        }
    }
}
