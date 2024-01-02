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
     * 默认字符串
     *
     * @see MapperStatementParamValueType#getType()
     */
    private int type = MapperStatementParamValueType.STRING.getType();

    public ParamMeta(String name) {
        this.name = name;
    }

    public ParamMeta(String name, MapperStatementParamValueType msPvType) {
        this.name = name;
        this.type = msPvType.getType();
    }
}
