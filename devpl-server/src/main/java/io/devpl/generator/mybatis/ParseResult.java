package io.devpl.generator.mybatis;

import io.devpl.generator.mybatis.tree.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.mapping.MappedStatement;

@Data
@AllArgsConstructor
public class ParseResult {

    private TreeNode<String> root;
    private MappedStatement mappedStatement;
}
