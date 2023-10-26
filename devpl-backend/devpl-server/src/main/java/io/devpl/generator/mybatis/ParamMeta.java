package io.devpl.generator.mybatis;

import io.devpl.generator.enums.MapperStatementParamValueType;
import lombok.Data;

@Data
public class ParamMeta {

    private String name;

    /**
     * @see MapperStatementParamValueType#getType()
     */
    private int type;

    public ParamMeta(String name) {
        this.name = name;
    }
}
