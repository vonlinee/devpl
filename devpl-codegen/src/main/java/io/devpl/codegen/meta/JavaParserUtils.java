package io.devpl.codegen.meta;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import io.devpl.codegen.utils.FieldsData;
import io.devpl.codegen.utils.JSONUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * https://houbb.github.io/2020/05/29/java-ast-06-comments
 * @author wangliang
 * Created On 2022-12-29 10:11:33
 */
public class JavaParserUtils {

    private static final JavaParser JAVA_PARSER_INSTANCE;
    // 打印配置
    static PrinterConfiguration printerConfiguration;

    static {
        ParserConfiguration parserConfig = new ParserConfiguration();
        parserConfig.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8); // JDK8
        parserConfig.setCharacterEncoding(StandardCharsets.UTF_8); // 源代码字符编码
        // 关闭注释分析 默认情况下启用注释分析，禁用将加快解析速度，但在处理单个源文件时速度提升不明显，如果要解析大量文件，建议禁用。
        parserConfig.setAttributeComments(true);
        // 设置为孤立注释
        parserConfig.setDoNotAssignCommentsPrecedingEmptyLines(true);
        JAVA_PARSER_INSTANCE = new JavaParser(parserConfig);
    }

    static {
        printerConfiguration = new DefaultPrinterConfiguration();
        printerConfiguration.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS, true));
        printerConfiguration.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.PRINT_JAVADOC, true));
        printerConfiguration.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.COLUMN_ALIGN_FIRST_METHOD_CHAIN, true));
    }

    /**
     * 解析工程下的所有Java文件
     * @param path 工程根目录
     */
    public static void parseProject(String path) {
        Path root = Paths.get(path);
        ProjectRoot projectRoot = new ParserCollectionStrategy().collect(root);
        final List<SourceRoot> sourceRoots = projectRoot.getSourceRoots();
        sourceRoots.forEach(sourceRoot -> {
            try {
                sourceRoot.parseParallelized((localPath, absolutePath, result) -> {
                    if (result.isSuccessful()) {
                        Optional<CompilationUnit> resultOptional = result.getResult();
                        NodeList<TypeDeclaration<?>> typeDeclarations = resultOptional.map(CompilationUnit::getTypes)
                            .orElseGet(NodeList::new);
                        for (TypeDeclaration<?> typeDeclaration : typeDeclarations) {
                            TypeInfo typeInfo = TypeInfo.of(typeDeclaration);
                            typeInfo.setPath(absolutePath.toString());
                            TypeInfoRegistry.register(typeInfo);
                        }
                    }
                    return SourceRoot.Callback.Result.SAVE;
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static <T> Optional<T> parse(File file, CompilationUnitVisitor<T> visitor) {
        ParseResult<CompilationUnit> pr = parseResult(file);
        if (pr.isSuccessful()) {
            return pr.getResult().map(visitor::visit);
        }
        return Optional.empty();
    }

    public static <T> Optional<T> parse(Reader reader, CompilationUnitVisitor<T> visitor) {
        ParseResult<CompilationUnit> result = JAVA_PARSER_INSTANCE.parse(reader);
        if (result.isSuccessful()) {
            return result.getResult().map(visitor::visit);
        }
        return Optional.empty();
    }

    public static ParseResult<CompilationUnit> parseResult(File file) {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> result = null;
        try {
            result = javaParser.parse(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ParseResult<CompilationUnit> parseFile(File file) {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> result;
        try {
            result = javaParser.parse(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Optional<CompilationUnit> parseResult = result.getResult();
        if (parseResult.isPresent()) {
            // 每一个java文件就是一个编译单元
            CompilationUnit compilationUnit = parseResult.get();
            String packageName = null;
            final Optional<PackageDeclaration> packageDeclarationOptional = compilationUnit.getPackageDeclaration();
            if (packageDeclarationOptional.isPresent()) {
                final PackageDeclaration packageDeclaration = packageDeclarationOptional.get();
                packageName = packageDeclaration.getNameAsString();
            }
            NodeList<ImportDeclaration> imports = compilationUnit.getImports();
            ImportInfo importInfo = ImportInfo.extract(imports);
            importInfo.setPackageName(packageName);
            // 返回编译单元中的所有顶级类型声明
            NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
            for (TypeDeclaration<?> type : types) {
                if (type.isTopLevelType()) {
                    NodeList<BodyDeclaration<?>> members = type.getMembers();
                    for (BodyDeclaration<?> member : members) {
                        if (member.isFieldDeclaration()) {
                            FieldDeclaration fd = member.asFieldDeclaration();
                            if (fd.isStatic()) {
                            } else {
                                NodeList<VariableDeclarator> variables = fd.getVariables();
                                VariableDeclarator variableDeclarator = variables.get(0);
                                final Type variableDeclaratorType = variableDeclarator.getType();
                                if (variableDeclaratorType.isClassOrInterfaceType()) {
                                    ClassOrInterfaceType ciType = variableDeclaratorType.asClassOrInterfaceType();
                                    final SimpleName name = ciType.getName();
                                    ciType.getTypeArguments().ifPresent(typeArguments -> {
                                        // 判断泛型个数
                                        if (isCollectionType(name.getIdentifier())) {
                                            // 泛型都是 ClassOrInterfaceType，包括String，不会是基础类型
                                            // 泛型可能还存在嵌套泛型，比如List<Map<String, Object>
                                            Type typeArg = typeArguments.get(0);
                                            if (typeArg.isClassOrInterfaceType()) {
                                                ClassOrInterfaceType classOrInterfaceType = typeArg.asClassOrInterfaceType();
                                                SimpleName typeArgName = classOrInterfaceType.getName();
                                                String typeName = importInfo.get(typeArgName);
                                                System.out.println(typeName);
                                            }
                                        } else if (isMapType(name.getIdentifier())) {
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isCollectionType(String identifier) {
        return "List".equals(identifier) || "Set".equals(identifier) || "Collection".equals(identifier);
    }

    public static boolean isMapType(String identifier) {
        return identifier != null && identifier.contains("Map");
    }

    /**
     * 处理类型,方法,成员
     * @param node
     */
    public static void processNode(Node node) {
        if (node instanceof TypeDeclaration) {
            // 类型声明
            // do something with this type declaration
        } else if (node instanceof MethodDeclaration) {
            // 方法声明
            // do something with this method declaration
            String methodName = ((MethodDeclaration) node).getName().getIdentifier();
            System.out.println("方法: " + methodName);
        } else if (node instanceof FieldDeclaration) {
            // 成员变量声明
            // do something with this field declaration
            // 注释
            Comment comment = node.getComment().orElse(null);
            // 变量
            NodeList<VariableDeclarator> variables = ((FieldDeclaration) node).getVariables();
            SimpleName fieldName = variables.get(0).getName();
            if (comment != null) {
                System.out.print(handleComment(comment.getContent()));
            }
            System.out.print("\t");
            System.out.print(fieldName);
            System.out.println();
        }
        for (Node child : node.getChildNodes()) {
            processNode(child);
        }
    }

    private static boolean handleComment(String content) {
        return false;
    }

    public static String toString(CompilationUnit compilationUnit) {
        return compilationUnit.toString(printerConfiguration);
    }

    public static Name newTypeName(String typeName) {
        final int i = typeName.lastIndexOf(".");
        if (i < 0) {
            return new Name(typeName);
        }
        return new Name(new Name(typeName.substring(0, i)), typeName.substring(i + 1));
    }

    public static void json2ObjectSchema(String jsonStr, String packageName, String className) {
        final Map<String, Object> map = JSONUtils.toMap(jsonStr);
        final Name annoJsonAlias = newTypeName("com.fasterxml.jackson.annotation.JsonAlias");
        CompilationUnit cu = new CompilationUnit();
        cu.setPackageDeclaration(packageName);
        cu.addImport(new ImportDeclaration(annoJsonAlias, false, false));
        ClassOrInterfaceDeclaration book = cu.addClass(className);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            java.lang.reflect.Type fieldType;
            final Class<?> type = value.getClass();
            // JSON序列化不会序列化为基本类型，一般都是包装类
            if (type == Integer.class) {
                fieldType = Integer.class;
            } else if (type == String.class) {
                fieldType = String.class;
            } else {
                fieldType = String.class;
            }
            ParseResult<ClassOrInterfaceType> result = JAVA_PARSER_INSTANCE.parseClassOrInterfaceType(fieldType.getTypeName());

            result.getResult().ifPresent(data -> {
                FieldDeclaration field = book.addField(data, key, Modifier.Keyword.PRIVATE);
                NormalAnnotationExpr annotationExpr = new NormalAnnotationExpr();
                annotationExpr.setName(annoJsonAlias.getIdentifier());

                // 注意添加的顺序：注释在注解的前面，否则不会打印注释
                final JavadocComment fieldComment = new JavadocComment(key);
                field.addOrphanComment(fieldComment);

                final NodeList<MemberValuePair> annoMemberMap = new NodeList<>();
                final MemberValuePair annoMember = new MemberValuePair();
                annoMember.setName("value");
                annoMember.setValue(new StringLiteralExpr(key));
                annoMemberMap.add(annoMember);
                annotationExpr.setPairs(annoMemberMap);

                field.addAnnotation(annotationExpr);
            });
        }
        System.out.println(toString(cu));
    }

    public static List<FieldsData> getFieldsDataList(String filePath) {
        try (FileInputStream in = new FileInputStream(filePath)) {
            ParseResult<CompilationUnit> cu = JAVA_PARSER_INSTANCE.parse(in);
            System.out.println(cu);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
