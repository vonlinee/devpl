package io.devpl.codegen.generator.config;

import io.devpl.codegen.ConstVal;
import lombok.Getter;

/**
 * 内置的输出文件类型
 */
@Getter
public enum OutputFile {
    ENTITY(ConstVal.TEMPLATE_ENTITY_JAVA),
    SERVICE(ConstVal.TEMPLATE_SERVICE),
    SERVICE_IMPL(ConstVal.TEMPLATE_SERVICE_IMPL),
    MAPPER(ConstVal.TEMPLATE_MAPPER_JAVA),

    /**
     * MyBatis Xml Mapper文件
     */
    MAPPER_XML(ConstVal.TEMPLATE_MAPPER_XML),

    /**
     * Controller文件
     */
    CONTROLLER(ConstVal.CONTROLLER),
    /**
     * 父文件夹
     */
    PARENT,
    /**
     * 自定义文件类型
     */
    CUSTOM;

    private String template;

    OutputFile() {
    }

    OutputFile(String template) {
        this.template = template;
    }
}
