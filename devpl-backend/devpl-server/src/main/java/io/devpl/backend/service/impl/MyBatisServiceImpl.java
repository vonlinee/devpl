package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.devpl.backend.dao.MappedStatementItemMapper;
import io.devpl.backend.domain.MsParamNode;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.domain.param.MappedStatementListParam;
import io.devpl.backend.entity.MappedStatementItem;
import io.devpl.backend.entity.MappedStatementParamMappingItem;
import io.devpl.backend.service.CrudService;
import io.devpl.backend.service.MyBatisService;
import io.devpl.backend.service.ProjectService;
import io.devpl.backend.tools.mybatis.*;
import io.devpl.backend.utils.DruidSqlFormatter;
import io.devpl.backend.utils.PathUtils;
import io.devpl.backend.utils.SimpleSqlFormatter;
import io.devpl.backend.utils.SqlFormatter;
import io.devpl.codegen.parser.JavaParserUtils;
import io.devpl.codegen.util.TypeUtils;
import io.devpl.sdk.TreeNode;
import io.devpl.sdk.annotations.NotNull;
import io.devpl.sdk.collection.CaseInsensitiveKeyMap;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.lang.RuntimeIOException;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 直接解析本地项目所有文件
 */
@Slf4j
@Service
public class MyBatisServiceImpl implements MyBatisService {

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

    /**
     * key-项目根路径
     * value-缓存的Mapper实例
     */
    Map<String, Configuration> cache = new ConcurrentHashMap<>();
    DynamicMyBatisSupport mybatis = new DynamicMyBatisSupport();

    CaseInsensitiveKeyMap<SqlFormatter> sqlFormatterMap = new CaseInsensitiveKeyMap<>();

    {
        SimpleSqlFormatter simpleSqlFormatter = new SimpleSqlFormatter();
        sqlFormatterMap.put(simpleSqlFormatter.getId(), simpleSqlFormatter);
        DruidSqlFormatter druidSqlFormatter = new DruidSqlFormatter();
        sqlFormatterMap.put(druidSqlFormatter.getId(), druidSqlFormatter);
        sqlFormatterMap.put(SqlFormatter.NONE.getId(), SqlFormatter.NONE);
    }

    /**
     * 适配vxe-table的树形结构，根据id和parentId来确定层级关系
     *
     * @param content   MyBatis Mapper Statement
     * @param inferType 推断参数的类型
     * @return 参数节点列表
     */
    @Override
    public List<MsParamNode> getMapperStatementParams(String content, boolean inferType) {
        ParseResult result = this.parseMapperStatement(content, inferType, false);
        // 根节点不使用
        TreeNode<StatementParam> root = result.getRoot();
        final List<MsParamNode> rows = new ArrayList<>();
        if (root.hasChildren()) {
            // 每层的父节点
            Map<Integer, MsParamNode> parentNodeMap = new HashMap<>();
            int num = 0;
            for (TreeNode<StatementParam> node : root.getChildren()) {
                recursive(node, rows, 1, num++, parentNodeMap);
            }
        }
        // 排序
        rows.sort((o1, o2) -> {
            if (o1.hasChildren() && o2.hasChildren()) {
                return o1.getFieldKey().compareTo(o2.getFieldKey());
            } else if (o1.hasChildren()) {
                return -1; // 包含嵌套字段的在前
            }
            return o1.getFieldKey().compareTo(o2.getFieldKey());
        });

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
    private void recursive(TreeNode<StatementParam> currentNode, List<MsParamNode> rows, int level, int nextNum, Map<Integer, MsParamNode> parentMap) {
        StatementParam currentParam = currentNode.getData();
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

        if (parentNode.isLeaf() && currentParam.getDataType() != null) {
            parentNode.setDataType(currentParam.getDataType().getQualifier());
        }
        MsParamNode ppNode = parentMap.get(level - 1);
        if (ppNode != null) {
            parentNode.setParentId(ppNode.getId());
        }
        if (currentNode.hasChildren()) {
            nextNum = 0;
            for (TreeNode<StatementParam> child : currentNode.getChildren()) {
                recursive(child, rows, level + 1, nextNum++, parentMap);
            }
        }
    }

    /**
     * @param mapperStatement mapper statement 语句 XML
     * @param inferType       是否开启类型推断
     * @return 解析结果
     */
    @Override
    public ParseResult parseMapperStatement(String mapperStatement, boolean inferType, boolean enableCache) {
        XmlFragmentParseParam xmlFragmentParseParam = new XmlFragmentParseParam();
        xmlFragmentParseParam.setEnableCache(enableCache);
        xmlFragmentParseParam.setInferType(inferType);
        xmlFragmentParseParam.setXmlTagContent(mapperStatement);
        MappedStatementMetadata msm = mybatis.parseXmlStatement(xmlFragmentParseParam);
        return new ParseResult(buildParamTree(msm.getParams()), msm.getMappedStatement());
    }

    /**
     * @param ognlVar ognl变量列表  变量名称可能带有多级嵌套形式
     * @return 转化成树形结构
     */
    private TreeNode<StatementParam> buildParamTree(Collection<StatementParam> ognlVar) {
        TreeNode<StatementParam> forest = new TreeNode<>(new StatementParam("root"));
        TreeNode<StatementParam> current = forest;
        for (StatementParam expression : ognlVar) {
            TreeNode<StatementParam> root = current;
            // 包含嵌套结构则继续向下
            if (expression.getName().indexOf(".") > 0) {
                for (String data : expression.getName().split("\\.")) {
                    current = current.addChild(new StatementParam(data));
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
    public String getSqlOfMappedStatement(GetSqlParam param) {
        final Map<String, Object> map = getParamMapOfMappedStatements(param);
        ParseResult result = this.parseMapperStatement(param.getMapperStatement(), true, false);
        MappedStatement ms = result.getMappedStatement();
        MappedStatementSqlBuilder sqlBuilder = mybatis;
        // 预编译sql
        String resultSql = param.getReal() == 0 ? sqlBuilder.buildPreparedSql(ms, map) : sqlBuilder.buildExecutableSql(ms, map);
        if (param.needFormatSql()) {
            SqlFormatter sqlFormatter = sqlFormatterMap.getOrDefault(param.getFormatter(), SqlFormatter.NONE);
            resultSql = sqlFormatter.format(param.getDialect(), resultSql);
        }
        return resultSql;
    }

    /**
     * 填充Mapper Statement参数
     *
     * @param param 参数
     * @return 形成Map对象形式的参数, 嵌套Map形式
     */
    private Map<String, Object> getParamMapOfMappedStatements(GetSqlParam param) {
        List<TreeNode<MsParamNode>> treeNodes = buildParamNodeTree(param.getMsParams());
        Map<String, Object> map = new HashMap<>();
        for (TreeNode<MsParamNode> treeNode : treeNodes) {
            fillParamMap(treeNode, map);
        }
        return map;
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
                Integer parentId = curNode.getParentId();
                if (parentId == null) {
                    parentNodeMap.put(curNode.getId(), new TreeNode<>(curNode));
                } else {
                    if (parentNodeMap.containsKey(parentId)) {
                        parentNodeMap.get(parentId).addChild(curNode);
                    } else {
                        parentNodeMap.get(parentId).addChild(new MsParamNode());
                    }
                }
            } else {
                // 父节点
                final Integer nodeId = curNode.getId();
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
        final String literalValue = node.getLiteralValue();
        if (literalValue == null || literalValue.isEmpty()) {
            return null;
        }
        MSParamDataType paramValueType = node.getValueType();
        if (paramValueType == null) {
            paramValueType = MSParamDataType.valueOfTypeName(node.getDataType());
        }
        if (paramValueType == null) {
            paramValueType = getPramDataType(literalValue);
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
            case COLLECTION -> {
                if (literalValue == null) {
                    val = Collections.emptyList();
                } else {
                    String[] items = literalValue.split(",");
                    List<Object> valueItems = new ArrayList<>();
                    for (String item : items) {
                        if (item == null || item.isEmpty()) {
                            continue;
                        }
                        MSParamDataType dataType = getPramDataType(item);
                        if (dataType == MSParamDataType.NUMERIC) {
                            valueItems.add(Integer.parseInt(item));
                        } else if (dataType == MSParamDataType.STRING) {
                            valueItems.add(item);
                        }
                    }
                    val = valueItems;
                }
            }
            default -> val = literalValue;
        }
        return val;
    }

    @NotNull
    public MSParamDataType getPramDataType(String literalValue) {
        MSParamDataType paramValueType = null;
        // 根据字符串推断类型，结果只能是简单的类型，不会很复杂
        if (TypeUtils.isInteger(literalValue) || TypeUtils.isDouble(literalValue)) {
            paramValueType = MSParamDataType.NUMERIC;
        } else {
            // 非数字类型的其他类型都可以当做字符串处理
            // 推断失败
            paramValueType = MSParamDataType.STRING;
        }
        return paramValueType;
    }

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
        ClassPathResource resource = new ClassPathResource("mybatis/mybatis-config.xml");
        Properties properties = new Properties();
        String environment = "";

        try (InputStream inputStream = resource.getInputStream()) {
            XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
            Configuration configuration = parser.parse();
            try (Stream<Path> mapperFilesStream = Files.list(Path.of(resource.getFile().getParent(), "mybatis/mapping"))) {
                /**d
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
    public void buildMapperXmlIndexForProject(String projectRootDir, boolean reset) {
        File rootDir = new File(projectRootDir);
        if (!projectService.isProjectRootDirectory(rootDir)) {
            return;
        }

        projectService.analyse(rootDir);


        final Map<String, String> fileIndexMap = new HashMap<>();

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
                    String fileName = file.getFileName().toString();
                    // 重名文件
                    if (fileIndexMap.containsKey(fileName)) {
                        int count = 1;
                        String key = fileName + count;
                        while (fileIndexMap.containsKey(key)) {
                            key = fileName + count++;
                        }
                        fileIndexMap.put(key, fileName);
                    } else {
                        fileIndexMap.put(fileName, file.toAbsolutePath().toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw RuntimeIOException.wrap(e);
        }

        if (reset) {
            Set<String> pathsToRemove = CollectionUtils.toSet(mapperXmlFiles, File::getAbsolutePath);
            if (mappedStatementItemMapper.deleteByFile(pathsToRemove)) {
                log.info("删除MapperStatement索引成功");
            }
        } else {
            List<String> belongedFiles = mappedStatementItemMapper.listBelongedFiles();
            if (!CollectionUtils.isEmpty(belongedFiles)) {
                mapperXmlFiles.removeIf(file -> belongedFiles.contains(file.getAbsolutePath()));
            }
        }

        if (!mapperXmlFiles.isEmpty()) {
            List<MappedStatementItem> mappedStatements = new ArrayList<>();
            List<MappedStatementItem> paramMappings = new ArrayList<>();

            final ParamMappingVisitor paramMappingVisitor = new ParamMappingVisitor();
            paramMappingVisitor.setFileIndexMap(fileIndexMap);
            for (File mapperXmlFile : mapperXmlFiles) {
                List<MappedStatementItem> mappedStatementsOfSingleFile = this.parseMappedStatements(rootDir, mapperXmlFile);
                if (mappedStatementsOfSingleFile.isEmpty()) {
                    continue;
                }
                // Mapper名称
                final String mapperNamespace = mappedStatementsOfSingleFile.get(0).getNamespace();
                // 每个语句指明的参数类型
                Map<String, MappedStatementItem> mappedStatementItemMap = CollectionUtils.toMap(mappedStatementsOfSingleFile, MappedStatementItem::getStatementId);
                // 解析参数信息
                File namespaceFile = findNamespaceFile(rootDir, mapperNamespace);
                if (namespaceFile == null) {
                    log.error("未找到namespace指定的Java源文件 {}", mapperNamespace);
                    continue;
                }
                paramMappingVisitor.setMapperFile(namespaceFile);
                paramMappingVisitor.setMappedStatementMap(mappedStatementItemMap);

                List<MappedStatementParamMappingItem> paramMappingsOfSingleFile = JavaParserUtils.parse(namespaceFile, 17, paramMappingVisitor).orElse(Collections.emptyList());

                Map<String, List<MappedStatementParamMappingItem>> map = CollectionUtils.groupingBy(paramMappingsOfSingleFile, MappedStatementParamMappingItem::getMappedStatementId);

                mappedStatements.addAll(mappedStatementsOfSingleFile);
            }

            crudService.saveBatch(paramMappings);
            crudService.saveBatch(mappedStatements);
        }
    }

    @Override
    public List<String> listIndexedProjectRootPaths() {
        return mappedStatementItemMapper.listIndexedProjectRootPaths();
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
        try (Stream<Path> stream = Files.walk(root.toPath())) {
            File[] files = stream.filter(p -> Files.isDirectory(p) && !p.toString().contains("target") && PathUtils.contains(p, path))
                .map(Path::toFile).toArray(File[]::new);
            if (files.length > 0) {
                files = files[0].listFiles();
                if (files != null && files.length > 0) {
                    result = Arrays.stream(files).filter(file -> file.getName().contains(names[names.length - 1])).findFirst().orElse(null);
                }
            }
        } catch (IOException e) {
            throw RuntimeIOException.wrap(e);
        }
        return result;
    }

    /**
     * @param projectRoot 项目根路径
     * @param mapperFile  XxxMapper.xml文件
     * @return MappedStatementItem信息
     */
    private List<MappedStatementItem> parseMappedStatements(File projectRoot, File mapperFile) {
        List<MappedStatementItem> items = new ArrayList<>();
        try (InputStream inputStream = FileUtils.openInputStream(mapperFile)) {
            /**
             * MyBatis使用XPath进行XML的解析
             */
            XPathParser parser = new XPathParser(inputStream, false, null, new IgnoreDTDEntityResolver());
            XNode rootNode = parser.evalNode("/mybatis/mapping");

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
                item.setProjectRoot(projectRoot.getAbsolutePath());
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

            NodeList mapperNodes = document.getElementsByTagName("mybatis/mapping");
            for (int i = 0; i < mapperNodes.getLength(); i++) {
                Node item = mapperNodes.item(i);

                NodeList childNodes = item.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node msNode = childNodes.item(j);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    @Override
    public IPage<MappedStatementItem> pageIndexedMappedStatements(MappedStatementListParam param) {
        return mappedStatementItemMapper.selectPage(param, Wrappers.<MappedStatementItem>lambdaQuery().eq(StringUtils.hasText(param.getStatementType()), MappedStatementItem::getStatementType, param.getStatementType()).like(StringUtils.hasText(param.getStatementId()), MappedStatementItem::getStatementId, param.getStatementId()).like(StringUtils.hasText(param.getNamespace()), MappedStatementItem::getNamespace, param.getNamespace()));
    }
}
