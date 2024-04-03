package io.devpl.codegen.config;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.core.Context;
import io.devpl.codegen.core.TemplateBasedTargetFile;

/**
 * 内置的生成文件目标类型
 */
public enum BuiltinTargetFile implements TemplateBasedTargetFile {

    ENTITY(ConstVal.TEMPLATE_ENTITY_JAVA),
    SERVICE(ConstVal.TEMPLATE_SERVICE),
    SERVICE_IMPL(ConstVal.TEMPLATE_SERVICE),
    CONTROLLER(ConstVal.TEMPLATE_SERVICE),
    MAPPER(ConstVal.TEMPLATE_SERVICE),
    MAPPER_XML(ConstVal.TEMPLATE_SERVICE);

    private final String templateName;

    BuiltinTargetFile(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String getTemplate() {
        return templateName;
    }

    @Override
    public String getFilename(Context context) {
        return null;
    }

    @Override
    public String getExtension(Context context) {
        return null;
    }

    @Override
    public String getEncoding(Context context) {
        return null;
    }
}
