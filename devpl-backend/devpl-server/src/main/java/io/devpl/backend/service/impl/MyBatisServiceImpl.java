package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.devpl.backend.dao.MappedStatementItemMapper;
import io.devpl.backend.domain.MsParamNode;
import io.devpl.backend.domain.enums.MSParamDataType;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.domain.param.MappedStatementListParam;
import io.devpl.backend.entity.MappedStatementItem;
import io.devpl.backend.entity.MappedStatementParamMappingItem;
import io.devpl.backend.service.CrudService;
import io.devpl.backend.service.MyBatisService;
import io.devpl.backend.service.ProjectService;
import io.devpl.backend.tools.mybatis.*;
import io.devpl.backend.utils.PathUtils;
import io.devpl.backend.utils.SqlFormat;
import io.devpl.backend.utils.XmlNode;
import io.devpl.codegen.parser.JavaParserUtils;
import io.devpl.codegen.util.TypeUtils;
import io.devpl.common.utils.XMLUtils;
import io.devpl.sdk.TreeNode;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.lang.RuntimeIOException;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.ReflectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.ognl.*;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.sql.DataSource;
import javax.xml.parsers.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
    @Resource
    ProjectService projectService;

    // 线程安全
    DynamicMyBatisConfiguration configuration;
    MyMapperBuilderAssistant assistant;
    MapperStatementParser msParser = new MapperStatementParser();

    /**
     * 适配vxe-table的树形结构，根据id和parentId来确定层级关系
     *
     * @param content   MyBatis Mapper Statement
     * @param inferType 推断参数的类型
     * @return 参数节点列表
     */
    @Override
    public List<MsParamNode> getMapperStatementParams(String content, boolean inferType) {
        ParseResult result = this.parseMapperStatement(content, inferType);
        // 根节点不使用
        TreeNode<ParamMeta> root = result.getRoot();
        final List<MsParamNode> rows = new LinkedList<>();
        if (root.hasChildren()) {
            // 每层的父节点
            Map<Integer, MsParamNode> parentNodeMap = new HashMap<>();
            int num = 0;
            for (TreeNode<ParamMeta> node : root.getChildren()) {
                recursive(node, rows, 1, num++, parentNodeMap);
            }
        }
        return rows;
    }

    /**
     * 递归将树形结构转换为列表
     *
     * @param currentNode 当前节点
     * @param rows        存储转换结果
     * @param nextNum     单层下一个节点的编号
     * @param level       层级，从1开始
     */
    private void recursive(TreeNode<ParamMeta> currentNode, List<MsParamNode> rows, int level, int nextNum, Map<Integer, MsParamNode> parentMap) {
        ParamMeta currentParam = currentNode.getData();
        MsParamNode parentNode = parentMap.get(level);
        if (parentNode == null) {
            // 当前层未初始化父节点S
            parentNode = new MsParamNode();
            parentNode.setId(level * 10 + nextNum);
            parentNode.setFieldKey(currentParam.getName());
            parentNode.setLeaf(!currentNode.hasChildren());
            rows.add(parentNode);

            parentMap.put(level, parentNode);
        } else {
            if (Objects.equals(currentParam.getName(), parentNode.getFieldKey())) {
                // 该节点重复，继续递归
            } else {
                parentNode = new MsParamNode();
                parentNode.setId(level * 10 + nextNum);
                parentNode.setFieldKey(currentParam.getName());
                parentNode.setLeaf(!currentNode.hasChildren());
                rows.add(parentNode);
            }
        }

        if (parentNode.isLeaf() && currentParam.getMsDataType() != null) {
            parentNode.setDataType(currentParam.getMsDataType().getQualifier());
        }

        MsParamNode ppNode = parentMap.get(level - 1);
        if (ppNode != null) {
            parentNode.setParentId(ppNode.getId());
        }


        if (currentNode.hasChildren()) {
            nextNum = 0;
            for (TreeNode<ParamMeta> child : currentNode.getChildren()) {
                recursive(child, rows, level + 1, nextNum++, parentMap);
            }
        }
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
            try (PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql())) {
                /**
                 * 这里BoundSql.getSql() 获取的sql是预编译的sql,带占位符
                 * 后续会经过ParameterHandler处理进行参数填充
                 */
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
            log.error("获取真实sql出错", exception);
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
        /**
         * MappedStatement#getBoundSql每次返回的是不同的对象
         * @see MappedStatement#getBoundSql(Object)
         */
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

    @PostConstruct
    public void init() {
        configuration = new DynamicMyBatisConfiguration(sqlSessionFactory.getConfiguration());
        assistant = new MyMapperBuilderAssistant(configuration, null);
    }

    /**
     * 将字符串的statement解析为MappedStatement对象
     *
     * @param statement xml 包含<select/> <delete/> <update/> <insert/> 等标签
     * @return MappedStatement实例
     */
    @Override
    public MappedStatement parseMappedStatement(String statement) {
        MissingCompatiableStatementBuilder statementParser = new MissingCompatiableStatementBuilder(configuration, msParser.getNode(statement), assistant);
        /**
         * 解析结果会放到 Configuration里
         * @see DynamicMyBatisConfiguration#addMappedStatement(MappedStatement)
         */
        return statementParser.parseMappedStatement();
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
                    if (treeNode != null) {
                        treeNode.addChild(curNode);
                    }
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
        MSParamDataType paramValueType = node.getValueType();
        if (paramValueType == null) {
            paramValueType = MSParamDataType.valueOfTypeName(node.getDataType());
        }
        if (paramValueType == null) {
            // 根据字符串推断类型，结果只能是简单的类型，不会很复杂
            if (TypeUtils.isInteger(literalValue)) {
                paramValueType = MSParamDataType.NUMERIC;
            } else if (TypeUtils.isDouble(literalValue)) {
                paramValueType = MSParamDataType.NUMERIC;
            } else {
                // 非数字类型的其他类型都可以当做字符串处理
                // 推断失败
                paramValueType = MSParamDataType.STRING;
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
    private Object parseLiteralValue(String literalValue, MSParamDataType paramValueType) {
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
     *                       XMLConfigBuilder#mappersElement(XNode)
     *                       XMLConfigBuilder#parseConfiguration(XNode)
     */
    @Override
    public void buildMapperXmlIndexForProject(String projectRootDir) {
        File rootDir = new File(projectRootDir);
        if (!projectService.isProjectRootDirectory(rootDir)) {
            return;
        }

        projectService.analyse(rootDir);

        List<File> mapperXmlFiles = new ArrayList<>();
        try {
            Files.walkFileTree(rootDir.toPath(), new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    String pathname = dir.toString();
                    // 忽略Idea编译输出目录
                    if (pathname.contains("target")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String path = file.toString();
                    if (path.contains("Mapper.xml")) {
                        mapperXmlFiles.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw RuntimeIOException.wrap(e);
        }

        Set<String> belongedFiles = mappedStatementItemMapper.listBelongedFiles();
        if (!CollectionUtils.isEmpty(belongedFiles)) {
            mapperXmlFiles.removeIf(file -> belongedFiles.contains(file.getAbsolutePath()));
        }

        List<MappedStatementItem> mappedStatements = CollectionUtils.toFlatList(mapperXmlFiles, this::parseMappedStatements);

        // 解析参数信息
        for (int i = 0; i < mappedStatements.size(); i++) {
            MappedStatementItem item = mappedStatements.get(i);
            File namespaceFile = findNamespaceFile(rootDir, item.getNamespace());

            if (namespaceFile == null) {
                continue;
            }

            ParamMappingVisitor paramMappingVisitor = new ParamMappingVisitor(namespaceFile);

            List<MappedStatementParamMappingItem> paramMappings = JavaParserUtils.parse(namespaceFile, 17, paramMappingVisitor).orElse(Collections.emptyList());

            Map<String, List<MappedStatementParamMappingItem>> map = CollectionUtils.groupingBy(paramMappings, MappedStatementParamMappingItem::getMappedStatementId);

            crudService.saveBatch(paramMappings);
        }

        crudService.saveBatch(mappedStatements);
    }

    /**
     * 定位到namespace所在文件
     *
     * @param root      项目根路径
     * @param namespace namespace
     * @return java源文件
     */
    private File findNamespaceFile(File root, String namespace) {
        final String[] names = namespace.split("\\.");
        Path path = Paths.get("", names).getParent();
        File result = null;
        try {
            File[] files = Files.walk(root.toPath())
                .filter(p -> Files.isDirectory(p) && !p.toString().contains("target")).filter(p -> PathUtils.contains(p, path)).map(Path::toFile).toArray(File[]::new);
            if (files.length > 0) {

                files = files[0].listFiles();
                if (files != null && files.length > 0) {
                    result = Arrays.stream(files).filter(file -> file.getName().contains(names[names.length - 1])).findFirst().orElse(null);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * TODO 解析参数和返回值
     *
     * @param mapperFile XxxMapper.xml文件
     * @return MappedStatementItem信息
     */
    public List<MappedStatementItem> parseMappedStatements(File mapperFile) {
        List<MappedStatementItem> items = new ArrayList<>();
        try (InputStream inputStream = FileUtils.openInputStream(mapperFile)) {
            /**
             * MyBatis使用XPath进行XML的解析
             */
            XPathParser parser = new XPathParser(inputStream, false, null, new IgnoreDTDEntityResolver());
            XNode rootNode = parser.evalNode("/mapper");

            String namespace = rootNode.getStringAttribute("namespace");

            /**
             * @see XMLStatementBuilder#parseStatementNode()
             */
            for (XNode context : rootNode.getChildren()) {
                String nodeName = context.getNode().getNodeName();

                if (!isMappedStatementNode(nodeName)) {
                    continue;
                }

                String id = context.getStringAttribute("id");
                MappedStatementItem item = new MappedStatementItem();

                item.setId(identifierGenerator.nextUUID(null));
                item.setStatementId(namespace + "." + id);
                item.setBelongFile(mapperFile.getAbsolutePath());
                /**
                 * 如果包含<![CDATA[<]]>，那么结果XNode.toString()的结果不包含<![CDATA[]]>标签，只会包含其内容
                 * 解析器解析得到的文本不包含CDATA，因此这里的XML内容是有语法错误的
                 */
                XmlNode node = new XmlNode(context.getNode(), null);
                item.setStatement(String.valueOf(node));
                item.setStatementType(nodeName);
                item.setNamespace(namespace);
                item.setParamType(context.getStringAttribute("paramType"));
                item.setResultType(context.getStringAttribute("resultType"));

                items.add(item);
            }
        } catch (IOException e) {
            log.error("解析文件{}失败", mapperFile, e);
        }
        return items;
    }

    /**
     * 使用SAX进行解析
     *
     * @param mapperFile mapper xml文件
     * @return
     */
    public List<MappedStatementItem> parseMappedStatementsWithSaxParser(File mapperFile) {
        List<MappedStatementItem> items = new ArrayList<>();
        try (InputStream inputStream = FileUtils.openInputStream(mapperFile)) {
            // javax.xml.parsers.SAXParserFactory 原生api获取factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // javax.xml.parsers.SAXParser 原生api获取parse
            SAXParser saxParser = factory.newSAXParser();
            // 获取xmlReader
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new DefaultHandler() {

                private String currentName;

                private boolean startFlag;
                private boolean endFlag;

                final StringBuilder sb = new StringBuilder();

                /**
                 * 处理节点内容
                 * @param ch 当前元素的字符序列
                 * @param start 当前元素的字符
                 * @param length 当前元素的字符序列长度
                 */
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (startFlag) {
                        sb.append(ch);
                    }
                }

                /**
                 * 每次sax读取到一个element开始时都会调用这个方法
                 * @param qName 元素的标签名称
                 */
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    currentName = qName;
                    startFlag = isMappedStatementNode(qName);
                }

                /**
                 * 结束时还会调用一次characters方法
                 */
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (startFlag && qName.equals(currentName)) {
                        // 结束
                        endFlag = true;
                        System.out.println(sb);
                    }

                    if (endFlag) {
                        sb.setLength(0);
                        endFlag = false;
                    }
                }
            });
            xmlReader.parse(new InputSource(new FileReader(mapperFile)));
        } catch (IOException e) {
            log.error("解析文件{}失败", mapperFile, e);
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public static boolean isMappedStatementNode(String name) {
        return StringUtils.equalsAny(name, "select", "update", "insert", "delete");
    }

    /**
     * 使用dom解析，会将整个xml读入内存
     *
     * @param mapperFile mapper xml 文件
     * @return mapper标签
     */
    public List<MappedStatementItem> parseMappedStatementsWithDomParser(File mapperFile) {
        List<MappedStatementItem> items = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
            // 转换CDATA标签为纯文本，设为true保留CDATA标签
            factory.setCoalescing(true);
            factory.setExpandEntityReferences(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(mapperFile);

            NodeList mapperNodes = document.getElementsByTagName("mapper");
            for (int i = 0; i < mapperNodes.getLength(); i++) {
                org.w3c.dom.Node item = mapperNodes.item(i);

                NodeList childNodes = item.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    org.w3c.dom.Node msNode = childNodes.item(j);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    /**
     * MyBatis使用XML语法，转义字符如下
     * &lt;<  小于号；
     * &gt;> 大于号；
     * &amp; & 和 ；
     * &apos;  ‘’单引号；
     * &quot; “”  双引号；
     *
     * @param content XML内容
     * @return 未转义的字符
     */
    public String toRawUnEscapedContent(String content) {
        content = content.replace("<", XMLUtils.wrapWithCDATA("<"));
        return content;
    }

    @Override
    public IPage<MappedStatementItem> pageIndexedMappedStatements(MappedStatementListParam param) {
        return mappedStatementItemMapper.selectPage(param, Wrappers.<MappedStatementItem>lambdaQuery()
            .eq(StringUtils.hasText(param.getStatementType()), MappedStatementItem::getStatementType, param.getStatementType())
            .like(StringUtils.hasText(param.getStatementId()), MappedStatementItem::getStatementId, param.getStatementId())
            .like(StringUtils.hasText(param.getNamespace()), MappedStatementItem::getNamespace, param.getNamespace()));
    }
}
