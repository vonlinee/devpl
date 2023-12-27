package io.devpl.backend.mybatis;

import io.devpl.backend.domain.enums.MapperStatementParamValueType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
