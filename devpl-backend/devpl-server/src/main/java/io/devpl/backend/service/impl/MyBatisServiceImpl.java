package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.devpl.backend.dao.MappedStatementItemMapper;
import io.devpl.backend.domain.MsParamNode;
import io.devpl.backend.domain.enums.MapperStatementParamValueType;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.domain.param.MappedStatementListParam;
import io.devpl.backend.entity.MappedStatementItem;
import io.devpl.backend.mybatis.*;
import io.devpl.backend.service.CrudService;
import io.devpl.backend.service.MyBatisService;
import io.devpl.backend.utils.SqlFormat;
import io.devpl.codegen.util.TypeUtils;
import io.devpl.sdk.TreeNode;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.util.ReflectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 直接解析本地项目所有文件
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
    @Resource
    CrudService crudService;
    @Resource
    IdentifierGenerator identifierGenerator;
    @Resource
    MappedStatementItemMapper mappedStatementItemMapper;

    @Override
    public List<MsParamNode> getMapperStatementParams(String content, boolean inferType) {
        ParseResult result = this.parseMapperStatement(content, inferType);
        // 根节点不使用
        TreeNode<ParamMeta> root = result.getRoot();
        final List<MsParamNode> rows = new LinkedList<>();
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
    private Set<ParamMeta> getOgnlVar(MappedStatement mappedStatement) {
        SqlSource sqlSource = mappedStatement.getSqlSource();
        Map<String, ParamMeta> result = new HashMap<>();
        if (sqlSource instanceof DynamicSqlSource dss) {
            SqlNode rootNode = ReflectionUtils.getTypedValue(dss, "rootSqlNode", null);
            searchParams(rootNode, result, null);
        }
        return new HashSet<>(result.values());
    }

    /**
     * @param ognlVar ognl变量列表  变量名称可能带有多级嵌套形式
     * @return 转化成树形结构
     */
    private TreeNode<ParamMeta> tree(Set<ParamMeta> ognlVar) {
        TreeNode<ParamMeta> forest = new TreeNode<>(new ParamMeta("root"));
        TreeNode<ParamMeta> current = forest;
        for (ParamMeta expression : ognlVar) {
            TreeNode<ParamMeta> root = current;
            // 包含嵌套结构则继续向下
            if (expression.getName().indexOf(".") > 0) {
                for (String data : expression.getName().split("\\.")) {
                    current = current.addChild(new ParamMeta(data));
                }
                current = root;
            } else {
                // 直接添加
                current.addChild(expression);
            }
        }
        return forest;
    }

    @Override
    public String getExecutableSql(MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        ParameterHandler parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        try (Connection connection = dataSource.getConnection()) {
            // 获取数据源
            try (PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql())) {
                parameterHandler.setParameters(preparedStatement);
                String sql = preparedStatement.toString();
                int index = sql.indexOf(":");
                if (index >= 0) {
                    sql = sql.substring(index + 1);
                }
                sql = sql.replace("\n", "").replace("\t", "");
                return SqlFormat.mysql(sql);
            }
        } catch (Exception exception) {
            log.error("获取真实sql出错");
        }
        return "解析失败";
    }

    @Override
    public String getSqlOfMappedStatement(GetSqlParam param) {
        List<TreeNode<MsParamNode>> treeNodes = buildParamNodeTree(param.getMsParams());
        Map<String, Object> map = new HashMap<>();
        for (TreeNode<MsParamNode> treeNode : treeNodes) {
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
     * @param statement xml 包含<select/> <delete/> <update/> <insert/> 等标签
     * @return MappedStatement实例
     */
    @Override
    public MappedStatement parseMappedStatement(String statement) {
        XPathParser xPathParser = new XPathParser(statement, false, null, new IgnoreDTDEntityResolver());
        // TODO 支持所有类型的SQL标签
        XNode selectNode = xPathParser.evalNode("select");
        MyBatisConfiguration configuration = new MyBatisConfiguration(sqlSessionFactory.getConfiguration());
        MyXmlStatementBuilder statementParser = new MyXmlStatementBuilder(configuration, selectNode);
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
    private void fillParamMap(TreeNode<MsParamNode> node, Map<String, Object> paramMap) {
        if (node.hasChildren()) {
            Map<String, Object> childMap = new HashMap<>();
            for (TreeNode<MsParamNode> child : node.getChildren()) {
                fillParamMap(child, childMap);
            }
            paramMap.put(node.getData().getFieldKey(), childMap);
        } else {
            MsParamNode paramNode = node.getData();
            paramMap.put(paramNode.getFieldKey(), getParamValueByType(paramNode));
        }
    }

    /**
     * 递归将树形结构转换为列表
     *
     * @param parentNode 父节点
     * @param rows       存储转换结果
     * @param parentId   父节点ID
     */
    private void recursive(TreeNode<ParamMeta> parentNode, List<MsParamNode> rows, int parentId) {
        MsParamNode parentRow = new MsParamNode();
        parentRow.setId(rows.size());
        parentRow.setKey(rows.size());
        if (parentId != -1) {
            parentRow.setParentKey(parentId);
            parentRow.setParentId(parentId);
        }
        parentRow.setFieldKey(parentNode.getData().getName());
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
    private List<TreeNode<MsParamNode>> buildParamNodeTree(List<MsParamNode> params) {
        Map<Integer, TreeNode<MsParamNode>> parentNodeMap = new HashMap<>();
        for (MsParamNode curNode : params) {
            // 父节点为null则默认为-1
            if (curNode.isLeaf()) {
                Integer parentId = curNode.getParentKey();
                if (parentId == null) {
                    parentNodeMap.put(curNode.getKey(), new TreeNode<>(curNode));
                } else {
                    if (parentNodeMap.containsKey(parentId)) {
                        parentNodeMap.get(parentId).addChild(curNode);
                    } else {
                        parentNodeMap.get(parentId).addChild(new MsParamNode());
                    }
                }
            } else {
                // 父节点
                final Integer nodeId = curNode.getKey();
                if (parentNodeMap.containsKey(nodeId)) {
                    TreeNode<MsParamNode> treeNode = parentNodeMap.get(nodeId);
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
    public Object getParamValueByType(MsParamNode node) {
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
                paramValueType = MapperStatementParamValueType.NUMERIC;
            } else if (TypeUtils.isDouble(literalValue)) {
                paramValueType = MapperStatementParamValueType.NUMERIC;
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
            case NUMERIC -> val = Long.parseLong(literalValue);
            // TODO 转为集合类型
            case COLLECTION -> val = LocalDateTime.parse(literalValue);
            default -> val = literalValue;
        }
        return val;
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
                paramMetaMap.put(expression, new ParamMeta(expression, MapperStatementParamValueType.COLLECTION));
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
    public MapperStatementParamValueType inferType(String paramName) {
        MapperStatementParamValueType type = MapperStatementParamValueType.NULL;
        if (StringUtils.endsWithIgnoreCase(paramName, "name")) {
            type = MapperStatementParamValueType.STRING;
        } else if (StringUtils.endsWithIgnoreCase(paramName, "num")) {
            type = MapperStatementParamValueType.NUMERIC;
        }
        return type;
    }

    /**
     * key-项目根路径
     * value-缓存的Mapper实例
     */
    Map<String, Configuration> cache = new ConcurrentHashMap<>();

    @Override
    public String getContent(String projectId, String msId) {
        Configuration configuration = cache.get(projectId);
        if (configuration == null) {
            return "项目" + projectId + "还未进行缓存";
        }
        MappedStatement mappedStatement = configuration.getMappedStatement(msId);
        if (mappedStatement == null) {
            return "项目" + projectId + "不存在ID为" + msId;
        }
        return mappedStatement.getBoundSql(new HashMap<>()).getSql();
    }

    @Override
    public List<String> buildIndex(String projectRootDir) {
        ClassPathResource resource = new ClassPathResource("mybatis-config.xml");
        Properties properties = new Properties();
        String environment = "";

        try (InputStream inputStream = resource.getInputStream()) {
            XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
            Configuration configuration = parser.parse();
            try (Stream<Path> mapperFilesStream = Files.list(Path.of(resource.getFile().getParent(), "mapper"))) {
                /**
                 * mapper文件中的sql标签
                 * key为namespace + <sql>标签的id，val为对应的XNode
                 */
                Map<String, XNode> sqlFragments = new HashMap<>();
                mapperFilesStream.forEach(file -> {
                    // log.info("开始解析{}", file.toString());
                    try (InputStream is = Files.newInputStream(file)) {
                        XMLMapperBuilder builder = new XMLMapperBuilder(is, configuration, file.toAbsolutePath().toString(), sqlFragments);
                        builder.parse();
                    } catch (IOException ignored) {
                        log.error("解析{}失败", file);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            cache.put(projectRootDir, configuration);
            return configuration.getMappedStatements().stream().map(MappedStatement::getId).toList();
        } catch (Exception exception) {
            log.error("解析MyBatis配置出现错误", exception);
        }
        return Collections.emptyList();
    }

    /**
     * @param projectRootDir 项目根路径
     * @see XMLConfigBuilder#mappersElement(XNode)
     * @see XMLConfigBuilder#parseConfiguration(XNode)
     */
    @Override
    public void buildMapperXmlIndexForProject(String projectRootDir) {

        URL resource = this.getClass().getResource("/");

        File mapperLocationDir = new File(FileUtils.toFile(resource), "mapper");

        List<MappedStatementItem> items = new ArrayList<>();

        try (Stream<File> stream = Arrays.stream(Objects.requireNonNull(mapperLocationDir.listFiles()))) {
            stream.forEach(mapperFile -> {
                try (InputStream inputStream = FileUtils.openInputStream(mapperFile)) {
                    XPathParser parser = new XPathParser(inputStream, false, null, new IgnoreDTDEntityResolver());
                    XNode rootNode = parser.evalNode("/mapper");

                    String namespace = rootNode.getStringAttribute("namespace");

                    /**
                     * @see XMLStatementBuilder#parseStatementNode()
                     */
                    for (XNode context : rootNode.getChildren()) {
                        String nodeName = context.getNode().getNodeName();
                        String id = context.getStringAttribute("id");

                        MappedStatementItem item = new MappedStatementItem();
                        item.setId(identifierGenerator.nextUUID(null));
                        item.setStatementId(id);
                        item.setBelongFile(mapperFile.getAbsolutePath());
                        item.setStatement(context.toString());
                        item.setStatementType(nodeName);
                        item.setNamespace(namespace);
                        item.setParamType(context.getStringAttribute("paramType"));
                        item.setResultType(context.getStringAttribute("resultType"));

                        items.add(item);
                    }
                } catch (IOException e) {
                    log.error("", e);
                }
            });
        }
        crudService.saveBatch(items);
    }

    @Override
    public IPage<MappedStatementItem> pageIndexedMappedStatements(MappedStatementListParam param) {
        return mappedStatementItemMapper.selectPage(param, Wrappers.<MappedStatementItem>lambdaQuery()
            .eq(StringUtils.hasText(param.getStatementType()), MappedStatementItem::getStatementType, param.getStatementType())
            .eq(StringUtils.hasText(param.getStatementId()), MappedStatementItem::getStatementId, param.getStatementId())
            .like(StringUtils.hasText(param.getNamespace()), MappedStatementItem::getNamespace, param.getNamespace()));
    }
}
