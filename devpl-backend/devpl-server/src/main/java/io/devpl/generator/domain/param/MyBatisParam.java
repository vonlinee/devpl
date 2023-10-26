package io.devpl.generator.domain.param;

import lombok.Data;

@Data
public class MyBatisParam {

    /**
     * mapper statement 包含select, insert, update, delete标签
     */
    private String mapperStatement;

    /**
     * 开启自动类型推断
     */
    private Boolean enableTypeInference;
}
