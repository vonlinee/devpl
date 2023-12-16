package io.devpl.codegen.core;

import io.devpl.codegen.config.IntrospectedTable;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public interface Plugin {

    void setContext(Context context);

    void setProperties(Properties properties);

    default List<GeneratedFile> generateFiles(IntrospectedTable table) {
        return Collections.emptyList();
    }
}
