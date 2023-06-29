package io.devpl.generator.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.devpl.generator.mybatis.tree.TreeNode;
import org.apache.ibatis.mapping.MappedStatement;

@Data
@AllArgsConstructor
public class ParseResult {

    private TreeNode<String> root;
    private MappedStatement mappedStatement;
}
