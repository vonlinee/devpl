package io.devpl.backend.tools.mybatis;

import io.devpl.codegen.type.TypeInferenceStrategy;
import io.devpl.sdk.util.ReflectionUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.ognl.*;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.*;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * 通过 mybatis 提供的API解析参数节点
 */
@Slf4j
public class DefaultMappedStatementParamExtractor implements MappedStatementParamExtractor {

    TypeInferenceStrategy<StatementParam> typeInferenceStrategy;

    Collection<StatementParam> params;

    @Override
    public void setTypeInferenceStrategy(TypeInferenceStrategy<StatementParam> strategy) {
        this.typeInferenceStrategy = strategy;
    }

    @Override
    public void apply(MappedStatement mappedStatement) {
        final Map<String, StatementParam> results = new HashMap<>();
        // 每个参数名, 有多少个重复的
        final Map<String, Integer> duplicatedCounter = new HashMap<>();
        collectMappedStatementParams(mappedStatement, results, duplicatedCounter);

        HashMap<String, Integer> _duplicatedCounter = new HashMap<>(duplicatedCounter);

        // 去重
        removeDuplicate(results, duplicatedCounter, (p1, p2) -> Objects.equals(p1.getType(), p2.getType())
                                                                && Objects.equals(p1.getOperator(), p2.getOperator())
                                                                && Objects.equals(p1.getValue(), p2.getValue())
                                                                && Objects.equals(p1.getDataType(), p2.getDataType()));


        _duplicatedCounter.entrySet().removeIf(e -> !duplicatedCounter.containsKey(e.getKey()));

        Collection<StatementParam> values = results.values();
        if (this.typeInferenceStrategy != null) {
            typeInferenceStrategy.inferType(values);
            // 再次去重
            removeDuplicate(results, _duplicatedCounter, (p1, p2) -> Objects.equals(p1.getType(), p2.getType()));
        }

        params = results.values();
    }

    private void removeDuplicate(Map<String, StatementParam> result, Map<String, Integer> duplicatedCounter, BiPredicate<StatementParam, StatementParam> condition) {
        final List<StatementParam> left = new ArrayList<>();
        // 合并重复的
        for (Map.Entry<String, Integer> entry : duplicatedCounter.entrySet()) {
            // 第一个参数
            final StatementParam param = result.get(entry.getKey());
            Integer count = entry.getValue();
            if (count == 0) {
                continue;
            }
            left.clear();
            for (int i = 1; i <= count; i++) {
                String paramName_i = entry.getKey() + i;
                StatementParam param_i = result.get(paramName_i);
                if (param_i == null) {
                    continue;
                }
                if (condition.test(param, param_i)) {
                    // 名称相同，类型相同 => 重复
                    result.remove(paramName_i);
                    // 计数减1
                    duplicatedCounter.computeIfPresent(entry.getKey(), (s, c) -> c - 1);
                } else {
                    left.add(param_i);
                }
            }

            // left.size() == duplicatedCounter.get(entry.getKey())

            if (left.size() > 1) {
                // 超过两个重复的
                while (left.size() > 1) {
                    int len = left.size();
                    boolean duplicated = false;
                    // 每轮两两比较
                    for (int i = 0; i < len; i++) {
                        if (i > left.size() - 1) {
                            break;
                        }
                        for (int j = i + 1; j < len; j++) {
                            if (j > left.size() - 1) {
                                break;
                            }
                            StatementParam param_i = left.get(i);
                            StatementParam param_j = left.get(j);
                            if (condition.test(param_i, param_j)) {
                                // 名称相同，类型相同 => 重复
                                result.remove(param_j.getName());
                                // 计数减1
                                duplicatedCounter.computeIfPresent(entry.getKey(), (s, c) -> c - 1);
                                left.remove(param_j);
                                duplicated = true;
                            }
                        }
                    }
                    // 本轮没有重复的，退出
                    if (!duplicated) {
                        break;
                    }
                }
            }
        }

        // 删除为0的项的计数
        duplicatedCounter.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue() == 0);
    }

    /**
     * 获取OGNL变量
     *
     * @param mappedStatement mapper语句标签
     */
    private void collectMappedStatementParams(MappedStatement mappedStatement, Map<String, StatementParam> result, Map<String, Integer> duplicatedCounter) {
        SqlSource sqlSource = mappedStatement.getSqlSource();
        if (sqlSource instanceof DynamicSqlSource dss) {
            SqlNode rootNode = ReflectionUtils.getTypedValue(dss, "rootSqlNode", null);
            searchParams(rootNode, result, null, duplicatedCounter);
        } else if (sqlSource instanceof RawSqlSource rss) {
            SqlSource ss = ReflectionUtils.getTypedValue(rss, "sqlSource", null);
            if (ss instanceof StaticSqlSource sss) {
                List<ParameterMapping> mappings = ReflectionUtils.getTypedValue(sss, "parameterMappings", null);
                for (ParameterMapping mapping : mappings) {
                    StatementParam statementParam = new StatementParam(mapping.getProperty(), MSParamDataType.fromType(mapping.getJavaType()));
                    result.put(mapping.getProperty(), statementParam);
                }
            }
        } else if (sqlSource instanceof ProviderSqlSource pss) {
            // ignore
        }
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
    private void searchParams(SqlNode parent, Map<String, StatementParam> paramMetaMap, String item, Map<String, Integer> duplicatedCounter) {
        if (parent instanceof MixedSqlNode msn) {
            // 混合节点类型
            List<SqlNode> contents = (List<SqlNode>) ReflectionUtils.getValue(msn, "contents");
            if (contents != null) {
                for (SqlNode content : contents) {
                    searchParams(content, paramMetaMap, item, duplicatedCounter);
                }
            }
        } else if (parent instanceof StaticTextSqlNode stsn) {
            // 文本类型节点 一般是没有被sql标签包围的文本部分
            String sqlText = (String) ReflectionUtils.getValue(stsn, "text");
            if (sqlText != null && !sqlText.isEmpty()) {
                sqlText = sqlText.trim();
                findVariableReference(sqlText, paramMetaMap, item, duplicatedCounter);
            }
        } else if (parent instanceof TextSqlNode tsn) {
            String sqlText = (String) ReflectionUtils.getValue(tsn, "text");
            if (sqlText != null && !sqlText.isEmpty()) {
                sqlText = sqlText.trim();
                findVariableReference(sqlText, paramMetaMap, item, duplicatedCounter);
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
                paramMetaMap.put(expression, new StatementParam(expression, MSParamDataType.COLLECTION));
                SqlNode contents = (SqlNode) ReflectionUtils.getValue(fesn, "contents");
                searchParams(contents, paramMetaMap, item, duplicatedCounter);
            }
        } else if (parent instanceof IfSqlNode ifsn) {
            // IfSqlNode会导致解析到的表达式重复
            // test 条件
            String testCondition = (String) ReflectionUtils.getValue(ifsn, "test");
            parseCondition(testCondition, paramMetaMap, duplicatedCounter);
            // 解析条件表达式中使用的表达式变量  Ognl表达式
            SqlNode content = (SqlNode) ReflectionUtils.getValue(ifsn, "contents");
            searchParams(content, paramMetaMap, item, duplicatedCounter);
        } else if (parent instanceof WhereSqlNode wsn) {
            SqlNode contents = (SqlNode) ReflectionUtils.getValue(wsn, "contents");
            searchParams(contents, paramMetaMap, item, duplicatedCounter);
        } else if (parent instanceof SetSqlNode ssn) {
            SqlNode contents = (SqlNode) ReflectionUtils.getValue(ssn, "contents");
            searchParams(contents, paramMetaMap, item, duplicatedCounter);
        } else if (parent instanceof ChooseSqlNode csn) {
            List<SqlNode> ifSqlNodes = (List<SqlNode>) ReflectionUtils.getValue(csn, "ifSqlNodes");
            if (ifSqlNodes != null) {
                SqlNode defaultSqlNode = (SqlNode) ReflectionUtils.getValue(csn, "defaultSqlNode");
                if (defaultSqlNode != null) {
                    ifSqlNodes.add(defaultSqlNode);
                }
                for (SqlNode sqlNode : ifSqlNodes) {
                    searchParams(sqlNode, paramMetaMap, item, duplicatedCounter);
                }
            }
        } else {
            log.info("未处理的节点类型 {}", parent.getClass());
        }
    }

    /**
     * 解析条件表达式
     * <p>
     * 一般是<if></if>，<when></when>这两类标签
     *
     * @param testCondition 条件表达式
     * @param results       存放参数结果
     */
    private void parseCondition(String testCondition, Map<String, StatementParam> results, Map<String, Integer> duplicatedCounter) {
        try {
            SimpleNode node = (SimpleNode) Ognl.parseExpression(testCondition);
            if (node instanceof ExpressionNode expressionNode) {
                searchOgnlExpressionNode(expressionNode, results, duplicatedCounter);
            } else if (node instanceof ASTProperty astPropertyNode) {
                String paramName = astPropertyNode.toString();
                if (!results.containsKey(paramName)) {
                    results.put(paramName, new StatementParam(paramName));
                }
            } else {
                log.warn("if判断中未处理的节点类型 {}", node.getClass());
            }
        } catch (OgnlException e) {
            log.error("failed to parse condition string {}", testCondition, e);
        }
    }

    private void searchOgnlExpressionNode(SimpleNode expressionNode, Map<String, StatementParam> results, Map<String, Integer> duplicatedCounter) {
        if (expressionNode instanceof ExpressionNode) {
            // 比较
            if (expressionNode instanceof ASTNotEq notEq) {
                // 例如 age != 1 或者 param.age != 1
                parseComparisonExpression(notEq, results, duplicatedCounter);
            } else if (expressionNode instanceof ASTAnd andNode) {
                searchChildren(andNode, results, duplicatedCounter);
            } else if (expressionNode instanceof ASTEq eqNode) {
                // 例如 age == 1 或者 param.age == 1
                parseComparisonExpression(eqNode, results, duplicatedCounter);
            }
        } else if (expressionNode instanceof ASTChain chainNode) {
            // 例如: param.name
            String paramName = calculateParamNameAndIncrementCount(chainNode.toString(), results, duplicatedCounter);
            results.put(paramName, new StatementParam(paramName));
        }
    }

    /**
     * 解析比较表达式
     * 例如 age != 1 或者 param.age != 1
     * 例如 age == 1 或者 param.age == 1
     *
     * @param expression 比较表达式
     * @param results    结果
     */
    private void parseComparisonExpression(ComparisonExpression expression, Map<String, StatementParam> results, Map<String, Integer> duplicatedCounter) {
        Node node1 = expression.jjtGetChild(0);
        StatementParam param = null;
        if (node1 instanceof ASTProperty ast1) {
            String paramName = ast1.toString();
            paramName = calculateParamNameAndIncrementCount(paramName, results, duplicatedCounter);
            param = new StatementParam(paramName);
        } else if (node1 instanceof ASTChain ast1) {
            String paramName = ast1.toString();
            paramName = calculateParamNameAndIncrementCount(paramName, results, duplicatedCounter);
            param = new StatementParam(paramName);
        }
        if (param == null) {
            return;
        }
        String operator = expression.getExpressionOperator(0);
        param.setOperator(operator);
        Node node2 = expression.jjtGetChild(1);
        if (node2 instanceof ASTConst astConstNode) {
            if ("null".equals(node2.toString()) && astConstNode.getValue() == null) {
                // 该字段类型为布尔值
                param.setDataType(MSParamDataType.BOOLEAN);
            } else {
                param.setValue(astConstNode.getValue());
            }
        }
        results.put(param.getName(), param);
    }

    private String calculateParamNameAndIncrementCount(String paramName, Map<String, StatementParam> results, Map<String, Integer> duplicatedCounter) {
        if (results.containsKey(paramName)) {
            Integer count = duplicatedCounter.get(paramName);
            if (count == null) {
                count = 1;
            } else {
                // 计数+1
                count++;
            }
            duplicatedCounter.put(paramName, count);
            paramName = paramName + count;
        }
        return paramName;
    }

    /**
     * 搜索子节点
     *
     * @param parent  父节点
     * @param results 保存结果
     */
    private void searchChildren(SimpleNode parent, Map<String, StatementParam> results, Map<String, Integer> duplicatedCounter) {
        int childrenCount = parent.jjtGetNumChildren();
        for (int i = 0; i < childrenCount; i++) {
            Node node = parent.jjtGetChild(i);
            searchOgnlExpressionNode((SimpleNode) node, results, duplicatedCounter);
        }
    }

    /**
     * 递归寻找$引用的表达式，对应的SqlNode是 TextSqlNode
     *
     * @param content 文本，包含${xxx}或者#{xxx}
     * @param results 存放结果的容器
     */
    private void findVariableReference(String content, Map<String, StatementParam> results, String item, Map<String, Integer> duplicatedCounter) {
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
                paramName = calculateParamNameAndIncrementCount(paramName, results, duplicatedCounter);
                StatementParam meta = new StatementParam(paramName);
                results.put(meta.getName(), meta);
                i = endIndex + 1;
            }
        }
    }

    @Override
    public Collection<StatementParam> getParams() {
        return this.params;
    }
}
