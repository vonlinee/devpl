package io.devpl.codegen.config;

/**
 * 内置的输出文件类型
 */
public enum OutputFile {
    ENTITY,
    SERVICE,
    SERVICE_IMPL,
    MAPPER,
    MAPPER_XML,
    CONTROLLER,

    /**
     * 自定义文件类型
     */
    CUSTOM,
    PARENT;
}
