package io.devpl.codegen.config;

/**
 * 内置的输出文件类型
 */
public enum OutputFile {
    ENTITY,
    SERVICE,
    SERVICE_IMPL,
    MAPPER,

    /**
     * MyBatis Xml Mapper文件
     */
    MAPPER_XML,

    /**
     * Controller文件
     */
    CONTROLLER,

    /**
     * 自定义文件类型
     */
    CUSTOM,
    PARENT
}
