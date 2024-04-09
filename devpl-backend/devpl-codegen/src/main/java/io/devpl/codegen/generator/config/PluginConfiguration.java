package io.devpl.codegen.generator.config;

import io.devpl.codegen.util.Messages;
import io.devpl.codegen.util.StringUtils;

import java.util.List;

public class PluginConfiguration extends TypedPropertyHolder {
    public PluginConfiguration() {
        super();
    }

    public void validate(List<String> errors, String contextId) {
        if (!StringUtils.hasText(getConfigurationType())) {
            errors.add(Messages.getString("ValidationError.17",
                contextId));
        }
    }
}
