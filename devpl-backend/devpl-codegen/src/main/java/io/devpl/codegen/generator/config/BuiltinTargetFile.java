package io.devpl.codegen.generator.config;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.generator.GeneratedFile;
import io.devpl.codegen.generator.GenerationTarget;
import io.devpl.codegen.generator.TemplateBasedTargetFile;

import java.util.Map;

/**
 * 内置的生成文件目标类型
 */
public enum BuiltinTargetFile implements TemplateBasedTargetFile {

    /**
     * Java 实体类
     */
    ENTITY_JAVA(ConstVal.TEMPLATE_ENTITY_JAVA) {
        @Override
        public Map<String, Object> getTemplateArguments(GenerationTarget target) {
            return null;
        }

        @Override
        public String getExtension(GenerationTarget target) {
            return ConstVal.JAVA_SUFFIX;
        }

        @Override
        public String getFilename(GenerationTarget target) {
            return null;
        }
    },
    /**
     * Kotlin 实体类
     */
    ENTITY_KOTLIN(ConstVal.TEMPLATE_ENTITY_KT) {
        @Override
        public Map<String, Object> getTemplateArguments(GenerationTarget target) {
            return null;
        }

        @Override
        public String getExtension(GenerationTarget target) {
            return ConstVal.KT_SUFFIX;
        }

        @Override
        public String getFilename(GenerationTarget target) {
            return null;
        }
    },
    /**
     * MVC Service 层接口
     */
    SERVICE(ConstVal.TEMPLATE_SERVICE) {
        @Override
        public Map<String, Object> getTemplateArguments(GenerationTarget target) {
            return null;
        }

        @Override
        public String getExtension(GenerationTarget target) {
            return ConstVal.JAVA_SUFFIX;
        }

        @Override
        public String getFilename(GenerationTarget target) {
            return null;
        }
    },
    SERVICE_IMPL(ConstVal.TEMPLATE_SERVICE_IMPL) {
        @Override
        public Map<String, Object> getTemplateArguments(GenerationTarget target) {
            return null;
        }

        @Override
        public String getExtension(GenerationTarget target) {
            return ConstVal.JAVA_SUFFIX;
        }

        @Override
        public String getFilename(GenerationTarget target) {
            return null;
        }
    },
    CONTROLLER(ConstVal.TEMPLATE_CONTROLLER) {
        @Override
        public Map<String, Object> getTemplateArguments(GenerationTarget target) {
            return null;
        }

        @Override
        public String getExtension(GenerationTarget target) {
            return ConstVal.JAVA_SUFFIX;
        }

        @Override
        public String getFilename(GenerationTarget target) {
            return null;
        }
    },
    MAPPER(ConstVal.TEMPLATE_MAPPER_JAVA) {
        @Override
        public Map<String, Object> getTemplateArguments(GenerationTarget target) {
            return null;
        }

        @Override
        public String getExtension(GenerationTarget target) {
            return ConstVal.JAVA_SUFFIX;
        }

        @Override
        public String getFilename(GenerationTarget target) {
            return null;
        }
    },
    MAPPER_XML(ConstVal.TEMPLATE_MAPPER_XML) {
        @Override
        public Map<String, Object> getTemplateArguments(GenerationTarget target) {
            return null;
        }

        @Override
        public String getExtension(GenerationTarget target) {
            return ConstVal.XML_SUFFIX;
        }

        @Override
        public String getFilename(GenerationTarget target) {
            return null;
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

    }

    @Override
    public void initialize(GenerationTarget target) {

    }

    @Override
    public String getTemplate() {
        return template;
    }
}
