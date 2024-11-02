package io.devpl.backend.tools.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class StatementParam {

    /**
     * 参数名, 可能是嵌套， 例如 param.name
     */
    private String name;

    /**
     * 默认字符串
     *
     * @see MSParamDataType#getType()
     */
    private int type = MSParamDataType.STRING.getType();

    private String operator;

    @Nullable
    private Object value;

    private MSParamDataType dataType;

    public StatementParam(String name) {
        this.name = name;
    }

    public StatementParam(String name, MSParamDataType msPvType) {
        this.name = name;
        this.type = msPvType.getType();
        this.dataType = msPvType;
    }

    public void setDataType(MSParamDataType dataType) {
        this.dataType = dataType;
        this.type = dataType.getType();
    }
}
