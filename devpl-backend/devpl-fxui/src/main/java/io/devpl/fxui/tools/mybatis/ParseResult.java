package io.devpl.fxui.tools.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.mapping.MappedStatement;

@Data
@AllArgsConstructor
public class ParseResult {

    private TreeNode<String> root;
    private MappedStatement mappedStatement;
}
