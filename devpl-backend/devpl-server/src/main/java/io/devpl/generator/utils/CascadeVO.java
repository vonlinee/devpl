package io.devpl.generator.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CascadeVO {

    /**
     * 组Id (组Id和叶子节点Id必须存在一个)
     */
    private String groupId;

    /**
     * 节点文本
     */
    private String label;

    /**
     * 文本后面的小字提示
     */
    private String remark;

    /**
     * 叶子节点Id(存在_groupId时该属性不起作用)
     */
    private String itemId;

    /**
     * 子节点数据
     */
    private List<CascadeVO> children;

    public void addChild(CascadeVO cascadeVO) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(cascadeVO);
    }
}
