package io.devpl.codegen.config;

import io.devpl.codegen.core.TableGeneration;

import java.util.Map;

public interface TemplateDataModelBean {

    Map<String, Object> renderData(TableGeneration tableInfo);
}
