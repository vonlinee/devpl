package io.devpl.backend.mybatis;

import io.devpl.backend.domain.enums.MSParamDataType;
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
     * @see MSParamDataType#getType()
     */
    private int type = MSParamDataType.STRING.getType();

    private MSParamDataType msDataType;

    public ParamMeta(String name) {
        this.name = name;
    }

    public ParamMeta(String name, MSParamDataType msPvType) {
        this.name = name;
        this.type = msPvType.getType();
        this.msDataType = msPvType;
    }
}
