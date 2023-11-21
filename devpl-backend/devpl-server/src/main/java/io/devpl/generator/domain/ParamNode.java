package io.devpl.generator.domain;

import io.devpl.generator.enums.MapperStatementParamValueType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * MyBatis Mapper Statement参数节点
 * <p>
 * antd和element-plus的树形表数据结构不同
 */
@Getter
@Setter
public class ParamNode {

    /**
     * 这里的ID无意义，只是作为一个唯一的序号
     * 前端树形表格组件使用
     * vxe-table使用id字段，react使用key字段
     */
    private Integer key;

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
     * 父节点ID
     * 前端树形表格组件使用
     */
    private Integer parentKey;

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数值
     */
    private Object value = "NULL";

    /**
     * 参数类型，枚举值
     */
    private String dataType;

    /**
     * 是否叶子结点
     */
    private boolean leaf;

    /**
     * 子节点
     */
    private List<ParamNode> children;

    private MapperStatementParamValueType valueType;

    public void setDataType(String dataType) {
        this.dataType = dataType;
        this.valueType = MapperStatementParamValueType.valueOfTypeName(dataType);
    }
}
