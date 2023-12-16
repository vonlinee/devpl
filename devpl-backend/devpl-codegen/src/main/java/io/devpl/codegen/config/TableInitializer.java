package io.devpl.codegen.config;

import java.util.Map;

public interface TableInitializer {

    Map<String, Object> renderData(IntrospectedTable tableInfo);
}
