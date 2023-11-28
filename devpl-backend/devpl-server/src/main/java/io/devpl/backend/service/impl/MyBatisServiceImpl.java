package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.generator.util.TypeUtils;
import io.devpl.backend.domain.ParamNode;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.domain.enums.MapperStatementParamValueType;
import io.devpl.backend.mybatis.*;
import io.devpl.backend.mybatis.tree.TreeNode;
import io.devpl.backend.service.MyBatisService;
import io.devpl.backend.utils.ReflectionUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.ognl.*;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TODO 直接解析本地项目所有文件
 */
@Slf4j
@Service
public class MyBatisServiceImpl implements MyBatisService {

    /**
     * 本系统自身的SqlSessionFactory
     */
    @Resource
    SqlSessionFactory sqlSessionFactory;

    @Resource
    DataSource dataSource;

    @Override
    public List<ParamNode> getMapperStatementParams(String content, boolean inferType) {
        ParseResult result = this.parseMapperStatement(content, inferType);
        // 根节点不使用
        TreeNode<ParamMeta> root = result.getRoot();
        final List<ParamNode> rows = new LinkedList<>();
        if (root.hasChildren()) {
            for (TreeNode<ParamMeta> node : root.getChildren()) {
                this.recursive(node, rows, -1);
            }
        }
        return rows;
    }

    @Override
    public ParseResult parseMapperStatement(String mapperStatement, boolean inferType) {
        // 直接获取XML中的节点
        MappedStatement mappedStatement = parseMappedStatement(mapperStatement);
        Set<ParamMeta> ognlVar = getOgnlVar(mappedStatement);
        return new ParseResult(tree(ognlVar), mappedStatement);
    }

    /**
     * 获取OGNL变量
     *
     * @param mappedStatement mapper语句标签
     * @return ognl变量列表
     */
    public Set<ParamMeta> getOgnlVar(MappedStatement mappedStatement) {
        SqlSource sqlSource = mappedStatement.getSqlSource();
        HashSet<ParamMeta> result = new HashSet<>();
        if (sqlSource instanceof DynamicSqlSource dss) {
            SqlNode rootNode = ReflectionUtils.getTypedValue(dss, "rootSqlNode", null);
            searchExpressions(rootNode, result);
        }
        return result;
    }

    /**
     * @param ognlVar ognl变量列表
     * @return 转化成树形结构
     */
    public TreeNode<ParamMeta> tree(Set<ParamMeta> ognlVar) {
        TreeNode<ParamMeta> forest = new TreeNode<>(new ParamMeta("root"));
        TreeNode<ParamMeta> current = forest;
        for (ParamMeta expression : ognlVar) {
            TreeNode<ParamMeta> root = current;
            for (String data : expression.getName().split("\\.")) {
                current = current.addChild(new ParamMeta(data));
            }
            current = root;
        }
        return forest;
    }

    @Override
    public String getExecutableSql(MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        ParameterHandler parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        try {
            // 获取数据源
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            parameterHandler.setParameters(preparedStatement);
            String sql = preparedStatement.toString();
            int index = sql.indexOf(":");
            if (index >= 0) {
                sql = sql.substring(index + 1);
            }
            sql = sql.replace("\n", "").replace("\t", "");
            return SqlFormat.mysql(sql);
        } catch (Exception exception) {
            log.error("获取真实sql出错");
        }
        return "解析失败";
    }

    @Override
    public String getSqlOfMappedStatement(GetSqlParam param) {
        List<TreeNode<ParamNode>> treeNodes = buildParamNodeTree(param.getMsParams());
        Map<String, Object> map = new HashMap<>();
        for (TreeNode<ParamNode> treeNode : treeNodes) {
            fillParamMap(treeNode, map);
        }
        ParseResult result = this.parseMapperStatement(param.getMapperStatement(), true);
        MappedStatement ms = result.getMappedStatement();
        BoundSql boundSql = ms.getBoundSql(map);
        String resultSql;
        if (param.getReal() == 0) {
            // 预编译sql
            resultSql = SqlFormat.mysql(boundSql.getSql());
        } else {
            resultSql = this.getExecutableSql(ms, boundSql, map);
        }
        return resultSql;
    }

    /**
     * 将字符串的statement解析为MappedStatement对象
     *
     * @param statement xml
     * @return MappedStatement
     */
    @Override
    public MappedStatement parseMappedStatement(String statement) {
        XPathParser xPathParser = new XPathParser(statement, false, null, new IgnoreDTDEntityResolver());
        // TODO 支持所有类型的SQL标签
        XNode selectNode = xPathParser.evalNode("select");
        MyBaticMockConfiguration configuration = new MyBaticMockConfiguration(sqlSessionFactory.getConfiguration());
        MyXmlStatemtnBuilder statementParser = new MyXmlStatemtnBuilder(configuration, selectNode);
        // 解析结果会放到 Configuration里
        statementParser.parseStatementNode();
        return configuration.getMappedStatements().stream().findFirst().orElse(null);
    }

    /**
     * 获取语句中的所有参数元数据
     *
     * @param mappedStatement mappedStatement
     * @return 参数元数据列表
     */
    @Override
    public List<ParamMeta> getParamMetadata(MappedStatement mappedStatement) {
        SqlSource sqlSource = mappedStatement.getSqlSource();
        Configuration configuration = new Configuration();
        DynamicContext visitor = new DynamicContextVisitor(configuration, new HashMap<>());
        if (sqlSource instanceof DynamicSqlSource dss) {
            SqlNode rootNode = ReflectionUtils.getTypedValue(dss, "rootSqlNode", null);
            rootNode.apply(visitor);
        }
        return new ArrayList<>();
    }

    @Override
    public List<ParamMeta> getParamMetadata(String statement) {
        MappedStatement mappedStatement = parseMappedStatement(statement);
        return getParamMetadata(mappedStatement);
    }

    /**
     * 将树形结构的参数节点放到嵌套Map中
     *
     * @param node     参数节点
     * @param paramMap 嵌套Map
     */
    private void fillParamMap(TreeNode<ParamNode> node, Map<String, Object> paramMap) {
        if (node.hasChildren()) {
            Map<String, Object> childMap = new HashMap<>();
            for (TreeNode<ParamNode> child : node.getChildren()) {
                fillParamMap(child, childMap);
            }
            paramMap.put(node.getData().getName(), childMap);
        } else {
            ParamNode paramNode = node.getData();
            paramMap.put(paramNode.getName(), getParamValueByType(paramNode));
        }
    }


    /**
     * 递归将树形结构转换为列表
     *
     * @param parentNode 父节点
     * @param rows       存储转换结果
     * @param parentId   父节点ID
     */
    @Override
    public void recursive(TreeNode<ParamMeta> parentNode, List<ParamNode> rows, int parentId) {
        ParamNode parentRow = new ParamNode();
        parentRow.setId(rows.size());
        parentRow.setKey(rows.size());
        if (parentId != -1) {
            parentRow.setParentKey(parentId);
            parentRow.setParentId(parentId);
        }
        parentRow.setName(parentNode.getData().getName());
        parentRow.setDataType(MapperStatementParamValueType.valueOfType(parentNode.getData().getType(), MapperStatementParamValueType.STRING).getQualifier());
        rows.add(parentRow);
        if (parentNode.hasChildren()) {
            for (TreeNode<ParamMeta> node : parentNode.getChildren()) {
                recursive(node, rows, parentRow.getKey());
            }
        } else {
            parentRow.setLeaf(true);
        }
    }

    /**
     * 将树形节点转换层单层map
     *
     * @param params 参数列表
     */
    private List<TreeNode<ParamNode>> buildParamNodeTree(List<ParamNode> params) {
        Map<Integer, TreeNode<ParamNode>> parentNodeMap = new HashMap<>();
        for (ParamNode curNode : params) {
            // 父节点为null则默认为-1
            if (curNode.isLeaf()) {
                Integer parentId = curNode.getParentKey();
                if (parentId == null) {
                    parentNodeMap.put(curNode.getKey(), new TreeNode<>(curNode));
                } else {
                    if (parentNodeMap.containsKey(parentId)) {
                        parentNodeMap.get(parentId).addChild(curNode);
                    } else {
                        parentNodeMap.get(parentId).addChild(new ParamNode());
                    }
                }
            } else {
                // 父节点
                final Integer nodeId = curNode.getKey();
                if (parentNodeMap.containsKey(nodeId)) {
                    TreeNode<ParamNode> treeNode = parentNodeMap.get(nodeId);
                    treeNode.getChildren().add(new TreeNode<>(curNode));
                } else {
                    parentNodeMap.put(nodeId, new TreeNode<>(curNode));
                }
            }
        }
        return new ArrayList<>(parentNodeMap.values());
    }

    /**
     * 界面上输入的值都是字符串
     * 参数都是使用#{}进行指定，在给sql填充参数时字符串会使用引号包裹
     * 而数字不需要使用引号包裹，因此需要推断数据类型
     *
     * @param node 参数表中的一行数据
     * @return 参数值，将字符串推断为某个数据类型，比如字符串类型的数字，将会转化为数字类型
     */
    public Object getParamValueByType(ParamNode node) {
        Object val = node.getValue();
        if (!(val instanceof String)) {
            return val;
        }
        final String literalValue = String.valueOf(val);
        MapperStatementParamValueType paramValueType = node.getValueType();
        if (paramValueType == null) {
            paramValueType = MapperStatementParamValueType.valueOfTypeName(node.getDataType());
        }
        if (paramValueType == null) {
            // 根据字符串推断类型，结果只能是简单的类型，不会很复杂
            if (TypeUtils.isInteger(literalValue)) {
                paramValueType = MapperStatementParamValueType.NUMBER;
            } else if (TypeUtils.isDouble(literalValue)) {
                paramValueType = MapperStatementParamValueType.NUMBER;
            } else {
                // 非数字类型的其他类型都可以当做字符串处理
                // 推断失败
                paramValueType = MapperStatementParamValueType.STRING;
            }
        }
        // 根据指定的类型进行赋值
        return parseLiteralValue(literalValue, paramValueType);
    }

    /**
     * 根据值类型将字符串解析为对应类型的值
     *
     * @param literalValue   字符串值
     * @param paramValueType 值类型
     * @return 对应类型 {@code paramValueType} 的值
     */
    private Object parseLiteralValue(String literalValue, MapperStatementParamValueType paramValueType) {
        Object val;
        switch (paramValueType) {
            case NUMBER -> val = Long.parseLong(literalValue);
            case COLLECTION -> val = LocalDateTime.parse(literalValue);
            default -> val = literalValue;
        }
        return val;
    }


    /**
     * 查找MyBatis的MappedStatement中所有出现的变量引用，只能出现文本中出现的变量
     * MyBatis未提供API来遍历，因此使用反射来获取
     *
     * @param parent      根节点
     * @param expressions 存放结果，未去重
     */
    @SuppressWarnings("unchecked")
    private void searchExpressions(SqlNode parent, Set<ParamMeta> expressions) {
        if (parent instanceof MixedSqlNode msn) {
            List<SqlNode> contents = (List<SqlNode>) ReflectionUtils.getValue(msn, "contents");
            if (contents != null) {
                for (SqlNode content : contents) {
                    searchExpressions(content, expressions);
                }
            }
        } else if (parent instanceof StaticTextSqlNode stsn) {
            String sqlText = (String) ReflectionUtils.getValue(stsn, "text");
            if (sqlText != null && !sqlText.isEmpty()) {
                find(sqlText, expressions);
            }
        } else if (parent instanceof TextSqlNode tsn) {
            String sqlText = (String) ReflectionUtils.getValue(tsn, "text");
            if (sqlText != null && !sqlText.isEmpty()) {
                find(sqlText, expressions);
            }
        } else if (parent instanceof ForEachSqlNode fesn) {
            // 集合类型
            String expression = (String) ReflectionUtils.getValue(fesn, "collectionExpression");
            expressions.add(new ParamMeta(expression));
            SqlNode contents = (SqlNode) ReflectionUtils.getValue(fesn, "contents");
            searchExpressions(contents, expressions);
        } else if (parent instanceof IfSqlNode ifsn) {
            // IfSqlNode会导致解析到的表达式重复
            // test 条件
            String testCondition = (String) ReflectionUtils.getValue(ifsn, "test");
            parseIfExpression(testCondition, expressions);
            // 解析条件表达式中使用的表达式变量  Ognl表达式
            SqlNode content = (SqlNode) ReflectionUtils.getValue(ifsn, "contents");
            searchExpressions(content, expressions);
        } else if (parent instanceof WhereSqlNode wsn) {
            SqlNode contents = (SqlNode) ReflectionUtils.getValue(wsn, "contents");
            searchExpressions(contents, expressions);
        } else if (parent instanceof SetSqlNode ssn) {
            SqlNode contents = (SqlNode) ReflectionUtils.getValue(ssn, "contents");
            searchExpressions(contents, expressions);
        } else if (parent instanceof ChooseSqlNode csn) {
            List<SqlNode> ifSqlNodes = (List<SqlNode>) ReflectionUtils.getValue(csn, "ifSqlNodes");
            if (ifSqlNodes != null) {
                SqlNode defaultSqlNode = (SqlNode) ReflectionUtils.getValue(csn, "defaultSqlNode");
                if (defaultSqlNode != null) {
                    ifSqlNodes.add(defaultSqlNode);
                }
                for (SqlNode sqlNode : ifSqlNodes) {
                    searchExpressions(sqlNode, expressions);
                }
            }
        }
    }

    private void parseIfExpression(String testCondition, Set<ParamMeta> expressions) {
        try {
            Object node = Ognl.parseExpression(testCondition);
            if (node instanceof ExpressionNode expressionNode) {
                searchOgnlExpressionNode(expressionNode, expressions);
            } else if (node instanceof ASTProperty astPropertyNode) {
                expressions.add(new ParamMeta(astPropertyNode.toString()));
            }
        } catch (OgnlException e) {
            // ignore
        }
    }

    private void searchOgnlExpressionNode(SimpleNode expressionNode, Set<ParamMeta> results) {
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
            results.add(new ParamMeta(chainNode.toString()));
        }
    }

    private void searchChildren(SimpleNode parent, Set<ParamMeta> results) {
        int childrenCount = parent.jjtGetNumChildren();
        for (int i = 0; i < childrenCount; i++) {
            Node node = parent.jjtGetChild(i);
            searchOgnlExpressionNode((SimpleNode) node, results);
        }
    }

    /**
     * 递归寻找$引用的表达式，对应的SqlNode是 TextSqlNode
     *
     * @param content     文本，包含${xxx}或者#{xxx}
     * @param expressions 存放结果的容器
     */
    private void find(String content, Set<ParamMeta> expressions) {
        content = content.trim().replace("\n", "");
        if (content.isEmpty()) {
            return;
        }
        final char[] chars = content.toCharArray();
        int fromIndex, endIndex;
        for (int i = 0; i < chars.length; i++) {
            // MyBatis要求 $和{之间没有空格才有效
            // 且不能嵌套
            // Mapper文件语法正确的情况下，一轮遍历即可，不会回头
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
                expressions.add(new ParamMeta(String.valueOf(Arrays.copyOfRange(chars, fromIndex, endIndex))));
                i = endIndex + 1;
            }
        }
    }

}
