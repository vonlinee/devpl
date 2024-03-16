package io.devpl.backend.tools.mybatis;

import io.devpl.backend.domain.enums.MSParamDataType;
import io.devpl.sdk.util.ReflectionUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.ognl.*;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.*;

import java.util.*;

@Slf4j
public class DefaultMappedStatementParamExtractor implements MappedStatementParamExtractor {

    Set<ParamMeta> params;

    @Override
    public void apply(MappedStatement mappedStatement) {
        params = getOgnlVar(mappedStatement);
    }

    /**
     * 获取OGNL变量
     *
     * @param mappedStatement mapper语句标签
     * @return ognl变量列表
     */
    private Set<ParamMeta> getOgnlVar(MappedStatement mappedStatement) {
        SqlSource sqlSource = mappedStatement.getSqlSource();
        Map<String, ParamMeta> result = new HashMap<>();
        if (sqlSource instanceof DynamicSqlSource dss) {
            SqlNode rootNode = ReflectionUtils.getTypedValue(dss, "rootSqlNode", null);
            searchParams(rootNode, result, null);
        } else if (sqlSource instanceof RawSqlSource rss) {
            SqlSource ss = ReflectionUtils.getTypedValue(rss, "sqlSource", null);
            if (ss instanceof StaticSqlSource sss) {
                List<ParameterMapping> mappings = ReflectionUtils.getTypedValue(sss, "parameterMappings", null);
                for (ParameterMapping mapping : mappings) {
                    ParamMeta paramMeta = new ParamMeta(mapping.getProperty(), MSParamDataType.fromType(mapping.getJavaType()));
                    result.put(mapping.getProperty(), paramMeta);
                }
            }
        }
        return new HashSet<>(result.values());
    }


    /**
     * 查找MyBatis的MappedStatement中所有出现的变量引用，只能出现文本中出现的变量
     * MyBatis未提供响应的API来遍历，因此使用反射来获取
     * 可以考虑通过SqlNode#apply方法来进行解析
     *
     * @param parent       根节点
     * @param paramMetaMap 存放结果
     * @param item         如果父节点是ForEachSqlNode，则item有值，为foreach标签里的 item 值
     * @see MappedStatement
     */
    @SuppressWarnings("unchecked")
    private void searchParams(SqlNode parent, Map<String, ParamMeta> paramMetaMap, String item) {
        if (parent instanceof MixedSqlNode msn) {
            // 混合节点类型
            List<SqlNode> contents = (List<SqlNode>) ReflectionUtils.getValue(msn, "contents");
            if (contents != null) {
                for (SqlNode content : contents) {
                    searchParams(content, paramMetaMap, item);
                }
            }
        } else if (parent instanceof StaticTextSqlNode stsn) {
            // 文本类型节点 一般是没有被sql标签包围的文本部分
            String sqlText = (String) ReflectionUtils.getValue(stsn, "text");
            if (sqlText != null && !sqlText.isEmpty()) {
                sqlText = sqlText.trim();
                findVariableReference(sqlText, paramMetaMap, item);
            }
        } else if (parent instanceof TextSqlNode tsn) {
            String sqlText = (String) ReflectionUtils.getValue(tsn, "text");
            if (sqlText != null && !sqlText.isEmpty()) {
                sqlText = sqlText.trim();
                findVariableReference(sqlText, paramMetaMap, item);
            }
        } else if (parent instanceof ForEachSqlNode fesn) {
            /**
             * 在使用mybatis的foreach遍历查询的时候，item属性起的名字不能跟后面的字段的参数名一样，否则会影响到查询的结果
             */
            String expression = (String) ReflectionUtils.getValue(fesn, "collectionExpression");
            // 应忽略item参数
            if (item == null) {
                item = ReflectionUtils.getTypedValue(fesn, "item", "");
            }
            if (!Objects.equals(expression, item)) {
                paramMetaMap.put(expression, new ParamMeta(expression, MSParamDataType.COLLECTION));
                SqlNode contents = (SqlNode) ReflectionUtils.getValue(fesn, "contents");
                searchParams(contents, paramMetaMap, item);
            }
        } else if (parent instanceof IfSqlNode ifsn) {
            // IfSqlNode会导致解析到的表达式重复
            // test 条件
            String testCondition = (String) ReflectionUtils.getValue(ifsn, "test");
            parseIfExpression(testCondition, paramMetaMap);
            // 解析条件表达式中使用的表达式变量  Ognl表达式
            SqlNode content = (SqlNode) ReflectionUtils.getValue(ifsn, "contents");
            searchParams(content, paramMetaMap, item);
        } else if (parent instanceof WhereSqlNode wsn) {
            SqlNode contents = (SqlNode) ReflectionUtils.getValue(wsn, "contents");
            searchParams(contents, paramMetaMap, item);
        } else if (parent instanceof SetSqlNode ssn) {
            SqlNode contents = (SqlNode) ReflectionUtils.getValue(ssn, "contents");
            searchParams(contents, paramMetaMap, item);
        } else if (parent instanceof ChooseSqlNode csn) {
            List<SqlNode> ifSqlNodes = (List<SqlNode>) ReflectionUtils.getValue(csn, "ifSqlNodes");
            if (ifSqlNodes != null) {
                SqlNode defaultSqlNode = (SqlNode) ReflectionUtils.getValue(csn, "defaultSqlNode");
                if (defaultSqlNode != null) {
                    ifSqlNodes.add(defaultSqlNode);
                }
                for (SqlNode sqlNode : ifSqlNodes) {
                    searchParams(sqlNode, paramMetaMap, item);
                }
            }
        } else {
            log.info("未处理的节点类型 {}", parent.getClass());
        }
    }

    /**
     * 解析if表达式中的参数
     *
     * @param testCondition 条件表达式
     * @param expressions   存放参数结果
     */
    private void parseIfExpression(String testCondition, Map<String, ParamMeta> expressions) {
        try {
            Object node = Ognl.parseExpression(testCondition);
            if (node instanceof ExpressionNode expressionNode) {
                searchOgnlExpressionNode(expressionNode, expressions);
            } else if (node instanceof ASTProperty astPropertyNode) {
                ParamMeta meta = new ParamMeta(astPropertyNode.toString());
                expressions.put(meta.getName(), meta);
            } else {
                log.warn("if判断中未处理的节点类型 {}", node.getClass());
            }
        } catch (OgnlException e) {
            // ignore
        }
    }

    private void searchOgnlExpressionNode(SimpleNode expressionNode, Map<String, ParamMeta> results) {
        if (expressionNode instanceof ExpressionNode) {
            // 比较
            if (expressionNode instanceof ASTNotEq notEq) {
                searchChildren(notEq, results);
            } else if (expressionNode instanceof ASTAnd andNode) {
                searchChildren(andNode, results);
            } else if (expressionNode instanceof ASTEq eqNode) {
                searchChildren(eqNode, results);
            }
        } else if (expressionNode instanceof ASTChain chainNode) {
            ParamMeta meta = new ParamMeta(chainNode.toString());
            results.put(meta.getName(), meta);
        }
    }

    /**
     * 搜索子节点
     *
     * @param parent  父节点
     * @param results 保存结果
     */
    private void searchChildren(SimpleNode parent, Map<String, ParamMeta> results) {
        int childrenCount = parent.jjtGetNumChildren();
        for (int i = 0; i < childrenCount; i++) {
            Node node = parent.jjtGetChild(i);
            searchOgnlExpressionNode((SimpleNode) node, results);
        }
    }

    /**
     * 递归寻找$引用的表达式，对应的SqlNode是 TextSqlNode
     *
     * @param content      文本，包含${xxx}或者#{xxx}
     * @param paramMetaMap 存放结果的容器
     */
    private void findVariableReference(String content, Map<String, ParamMeta> paramMetaMap, String item) {
        content = content.trim().replace("\n", "");
        if (content.isEmpty()) {
            return;
        }
        final char[] chars = content.toCharArray();
        int fromIndex, endIndex;
        for (int i = 0; i < chars.length; i++) {
            // MyBatis要求 $和{之间没有空格才有效 且不能嵌套
            if ((chars[i] == '$' || chars[i] == '#') && chars[i + 1] == '{') {
                // 找到}
                fromIndex = i + 2;
                endIndex = fromIndex + 1;
                while (chars[endIndex] != '}') {
                    if (chars[endIndex] == ' ') {
                        fromIndex++;
                    }
                    endIndex++;
                }
                String paramName = StringUtils.toString(chars, fromIndex, endIndex);
                if (Objects.equals(paramName, item)) {
                    continue;
                }
                ParamMeta meta = new ParamMeta(paramName);
                meta.setType(inferType(meta.getName()).getType());
                paramMetaMap.put(meta.getName(), meta);
                i = endIndex + 1;
            }
        }
    }

    /**
     * 根据名称推断类型
     * TODO 可以根据数据库字段来解析类型
     *
     * @param paramName 参数名称
     * @return Mapper参数类型
     */
    @Override
    public MSParamDataType inferType(String paramName) {
        MSParamDataType type = MSParamDataType.NULL;
        if (StringUtils.endsWithIgnoreCase(paramName, "name")) {
            type = MSParamDataType.STRING;
        } else if (StringUtils.endsWithIgnoreCase(paramName, "num")) {
            type = MSParamDataType.NUMERIC;
        }
        return type;
    }

    @Override
    public Set<ParamMeta> getParams() {
        return this.params;
    }
}
