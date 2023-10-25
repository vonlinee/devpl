package io.devpl.generator.mybatis;

import io.devpl.generator.mybatis.tree.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.mapping.MappedStatement;

@Data
@AllArgsConstructor
public class ParseResult {

    /**
     * 变量表，树形结构
     */
    private TreeNode<String> root;

    /**
     * MappedStatement
     */
    private MappedStatement mappedStatement;
}
