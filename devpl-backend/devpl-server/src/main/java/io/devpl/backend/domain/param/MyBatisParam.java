package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyBatisParam {

    /**
     * mapper statement 包含select, insert, update, delete标签
     */
    private String mapperStatement;

    /**
     * 开启自动类型推断
     */
    private Boolean enableTypeInference;

    private Boolean enableCache;

    /**
     * 根据参数名称推断类型
     */
    private Boolean inferByParamName;

    /**
     * Mapper Statement ID
     * 例如：org.example.ExampleMapper.selectById
     */
    private String mapperStatementId;

    public boolean isTypeInferEnabled() {
        return enableTypeInference == null || enableTypeInference;
    }

    public boolean isCacheEnabled() {
        return enableCache == null || enableCache;
    }
}
