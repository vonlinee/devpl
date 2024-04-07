package io.devpl.codegen.generator.config;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.generator.*;
import io.devpl.codegen.template.TemplateEngine;

/**
 * 内置的生成文件目标类型
 */
public enum BuiltinTargetFile implements TemplateBasedTargetFile {

    /**
     * Java 实体类
     */
    ENTITY_JAVA(ConstVal.TEMPLATE_ENTITY_JAVA) {
        @Override
        public String getExtension() {
            return ConstVal.JAVA_SUFFIX;
        }
    },
    /**
     * Kotlin 实体类
     */
    ENTITY_KOTLIN(ConstVal.TEMPLATE_ENTITY_KT) {
        @Override
        public String getExtension() {
            return ConstVal.KT_SUFFIX;
        }
    },
    /**
     * MVC Service 层接口
     */
    SERVICE(ConstVal.TEMPLATE_SERVICE) {
        @Override
        public String getExtension() {
            return ConstVal.JAVA_SUFFIX;
        }
    },
    SERVICE_IMPL(ConstVal.TEMPLATE_SERVICE_IMPL) {
        @Override
        public String getExtension() {
            return ConstVal.JAVA_SUFFIX;
        }
    },
    CONTROLLER(ConstVal.TEMPLATE_CONTROLLER) {
        @Override
        public String getExtension() {
            return ConstVal.JAVA_SUFFIX;
        }
    },
    MAPPER(ConstVal.TEMPLATE_MAPPER_JAVA) {
        @Override
        public String getExtension() {
            return ConstVal.JAVA_SUFFIX;
        }
    },
    MAPPER_XML(ConstVal.TEMPLATE_MAPPER_XML) {
        @Override
        public String getExtension() {
            return ConstVal.XML_SUFFIX;
        }
    };

    /**
     * 使用的模板路径
     */
    private final String template;

    BuiltinTargetFile(String template) {
        this.template = template;
    }

    @Override
    public String getName() {
        return super.name();
    }

    @Override
    public void initialize(GeneratedFile file) {
        if (file instanceof TemplateGeneratedFile tgf) {
            tgf.setTemplate(this.template);
        }
        file.setExtension(this.getExtension());
    }

    @Override
    public void initialize(GenerationTarget target) {

    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public FileGenerator getFileGenerator(Context context) {
        TemplateBasedFileGenerator generator = new TemplateBasedFileGenerator(this);
        TemplateEngine templateEngine = context.getObject(TemplateEngine.class);
        generator.setTemplateEngine(templateEngine);
        return generator;
    }
}
