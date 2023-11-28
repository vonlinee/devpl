package io.devpl.backend.boot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class CodeGenProperties {

    private final String templateDirectory = "codegen/templates";

    @Value("${devpl.codegen.template-location:}")
    private String templateLocation;

    /**
     * 代码生成根目录
     */
    @Value("${devpl.file.codegen.root}")
    private String codeGenRootDir;
}
