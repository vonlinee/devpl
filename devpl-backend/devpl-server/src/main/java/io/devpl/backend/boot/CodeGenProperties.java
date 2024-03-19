package io.devpl.backend.boot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CodeGenProperties {

    /**
     * 模板目录(项目的resources目录)，存放内置模板
     */
    @Value("${devpl.codegen.template-dir:codegen/templates}")
    private String templateDirectory;

    /**
     * 本地文件系统 模板存放位置，默认 ${user.home}/devpl/templates
     */
    @Value("${devpl.codegen.template.local-location:${user.home}/devpl/templates}")
    private String templateLocation;

    /**
     * 代码生成根目录
     */
    @Value("${devpl.file.codegen.root:${user.home}/devpl/codegen}")
    private String codeGenRootDir;
}
