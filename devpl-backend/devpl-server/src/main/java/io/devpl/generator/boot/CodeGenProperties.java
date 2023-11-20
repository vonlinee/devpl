package io.devpl.generator.boot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class CodeGenProperties {

    @Value("${devpl.codegen.template-location}")
    private String templateLocation;
}
