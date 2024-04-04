package io.devpl.codegen.generator.config;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.generator.Context;
import io.devpl.codegen.generator.GeneratedFile;
import io.devpl.codegen.generator.TemplateBasedTargetFile;
import io.devpl.codegen.generator.TemplateGeneratedFile;

/**
 * 内置的生成文件目标类型
 */
public enum BuiltinTargetFile implements TemplateBasedTargetFile {

    ENTITY_JAVA(ConstVal.TEMPLATE_ENTITY_JAVA),
    ENTITY_KOTLIN(ConstVal.TEMPLATE_ENTITY_KT),
    SERVICE(ConstVal.TEMPLATE_SERVICE),
    SERVICE_IMPL(ConstVal.TEMPLATE_SERVICE_IMPL),
    CONTROLLER(ConstVal.TEMPLATE_CONTROLLER),
    MAPPER(ConstVal.TEMPLATE_MAPPER_JAVA),
    MAPPER_XML(ConstVal.TEMPLATE_MAPPER_XML);

    private final String template;
    private volatile Context context;

    BuiltinTargetFile(String template) {
        this.template = template;
    }

    @Override
    public void initialize(GeneratedFile file) {
        if (file instanceof TemplateGeneratedFile tgf) {
            tgf.setTemplate(this.template);
        }
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
