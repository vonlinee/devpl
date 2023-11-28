package io.devpl.backend.mybatis;

import io.devpl.backend.domain.enums.MapperStatementParamValueType;
import lombok.Data;

@Data
public class ParamMeta {

    /**
     * 参数名
     */
    private String name;

    /**
     * @see MapperStatementParamValueType#getType()
     */
    private int type;

    public ParamMeta(String name) {
        this.name = name;
    }
}
