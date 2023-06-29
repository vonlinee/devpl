package io.devpl.generator.domain;

import io.devpl.generator.enums.MapperStatementParamValueType;
import io.devpl.sdk.lang.annotation.Nullable;
import lombok.Data;

/**
 * MyBatis Mapper Statement参数节点
 */
@Data
public class ParamNode {

    /**
     * 这里的ID无意义，只是作为一个唯一的序号
     * 前端树形表格组件使用
     */
    private Integer id;

    /**
     * 父节点ID
     * 前端树形表格组件使用
     */
    private Integer parentId;

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数值
     */
    private Object value;

    /**
     * 参数类型，枚举值
     */
    private String type;

    /**
     * 是否叶子结点
     */
    private boolean leaf;

    @Nullable
    private MapperStatementParamValueType valueType;

    public void setType(String type) {
        this.type = type;
        this.valueType = MapperStatementParamValueType.valueOfTypeName(type);
    }
}
