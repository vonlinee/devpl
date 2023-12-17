package io.devpl.codegen.config;

/**
 * 目标文件类型
 */
public enum TargetFileType {

    ENTITY,
    SERVICE,
    SERVICE_IMPL,
    CONTROLLER,
    MAPPER,
    MAPPER_XML;

    private String templateName;
}
