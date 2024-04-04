package io.devpl.codegen.generator.config;

import io.devpl.codegen.generator.TableGeneration;

import java.util.Map;

public interface TemplateDataModelBean {

    Map<String, Object> renderData(TableGeneration tableInfo);
}
