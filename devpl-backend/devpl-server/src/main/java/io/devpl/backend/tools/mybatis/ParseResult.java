package io.devpl.backend.tools.mybatis;

import io.devpl.sdk.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.MappedStatement;

@Getter
@Setter
@AllArgsConstructor
public class ParseResult {

    /**
     * 变量表，树形结构
     *
     * 比如
     * param.name
     * param.age
     * 结果为:
     * param
     *      -> name
     *      -> age
     */
    private TreeNode<ParamMeta> root;

    /**
     * MappedStatement
     */
    private MappedStatement mappedStatement;
}
