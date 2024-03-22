package io.devpl.backend.tools.mybatis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import io.devpl.backend.entity.MappedStatementItem;
import io.devpl.backend.entity.MappedStatementParamMappingItem;
import io.devpl.codegen.parser.java.CompilationUnitVisitor;
import io.devpl.codegen.parser.java.JavaASTUtils;
import io.devpl.codegen.parser.java.MetaField;
import io.devpl.codegen.db.SqlStatementType;
import io.devpl.sdk.util.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Slf4j
public class ParamMappingVisitor implements CompilationUnitVisitor<List<MappedStatementParamMappingItem>> {

    /**
     * Mapper.java 源文件
     */
    private File mapperFile;

    /**
     * Mapper.xml中存在的Mapper标签
     */
    private Map<String, MappedStatementItem> mappedStatementMap;

    private Map<String, String> fileIndexMap;

    public ParamMappingVisitor() {
        this.mappedStatementMap = new HashMap<>();
    }

    public ParamMappingVisitor(File file) {
        this.mapperFile = file;
    }

    /**
     * 数据类型过滤，内置的数据类型
     */
    private static final String[] DATA_TYPE_FILTER = new String[]{
        "Int",
        "Long",
        "String",
        "Character",
        "char",
        "CharSequence",
        "Double",
        "Float",
        "Short",
        "Byte",
        "Date",
        "LocalTime",
        "LocalDateTime",
        "Collection",
        "List",
        "Set"
    };

    public boolean isCustomType(String dataType) {
        if (dataType == null || dataType.isEmpty()) {
            return false;
        }
        for (String dt : DATA_TYPE_FILTER) {
            if (dt.charAt(0) == 'C' && dt.length() >= 10) {
                if (dataType.contains("Collection")) {
                    return false;
                }
            }
            if (dataType.equals(dt)) {
                return false;
            }
        }

        if (dataType.contains("[]")) {
            // 数组
            return false;
        }

        return true;
    }

    @Override
    public List<MappedStatementParamMappingItem> visit(CompilationUnit cu) {

        List<MappedStatementParamMappingItem> result = new ArrayList<>();

        final String packageName = getPackageName(cu);

        for (TypeDeclaration<?> type : cu.getTypes()) {
            final String mapperClassName = type.getNameAsString();

            for (MethodDeclaration method : type.getMethods()) {

                final String mapperMethodName = method.getNameAsString();

                final String msId = String.join(".", packageName, mapperClassName, mapperMethodName);

                if (!mappedStatementMap.containsKey(msId)) {
                    /**
                     * 检查是否是注解形式的Mapper
                     */
                    boolean isMapperMethod = false;
                    for (AnnotationExpr annotation : method.getAnnotations()) {
                        String annotationNameInMapperMethod = annotation.getNameAsString();
                        SqlStatementType statementType = SqlStatementType.findByName(annotationNameInMapperMethod);
                        if (SqlStatementType.isDml(statementType)) {
                            isMapperMethod = true;
                            break;
                        }
                    }
                    if (!isMapperMethod) {
                        continue;
                    }
                }

                for (Parameter parameter : method.getParameters()) {
                    MappedStatementParamMappingItem item = new MappedStatementParamMappingItem();
                    item.setMappedStatementId(msId);
                    item.setDeleted(false);
                    String paramName = null;
                    AnnotationExpr paramAnnotation = parameter.getAnnotationByName("Param").orElse(null);
                    if (paramAnnotation == null) {
                        paramName = parameter.getNameAsString();
                    } else {
                        if (paramAnnotation instanceof SingleMemberAnnotationExpr smae) {
                            Expression memberValueExpression = smae.getMemberValue();
                            if (memberValueExpression instanceof StringLiteralExpr sle) {
                                paramName = sle.getValue();
                            }
                        }
                    }
                    item.setParamName(paramName);
                    item.setParamType(parameter.getTypeAsString());

                    MappedStatementItem mappedStatementItem = mappedStatementMap.get(msId);
                    // 如果XML里配置了paramType，则直接使用该值作为参数类型
                    if (mappedStatementItem != null && StringUtils.hasText(mappedStatementItem.getParamType())) {
                        if (isCustomType(mappedStatementItem.getParamType())) {
                            // 解析该类型
                            System.out.println(mappedStatementItem.getParamType());
                        }
                    } else {
                        // 如果是自定义对象类型 则解析该对象的字段
                        if (isCustomType(item.getParamType()) && StringUtils.hasText(item.getParamType())) {
                            // 在项目根目录下搜索文件名为该类型的文件
                            String typeName = item.getParamType();
                            for (String key : fileIndexMap.keySet()) {
                                if (key.startsWith(typeName)) {
                                    // 加上.java后缀的长度
                                    if (key.length() != typeName.length() + 5) {
                                        if (!Character.isDigit(key.charAt(typeName.length()))) {
                                            continue;
                                        }
                                    }
                                    String path = fileIndexMap.get(key);
                                    File paramClassFile = new File(path);
                                    List<MetaField> fields = JavaASTUtils.parseFields(paramClassFile);

                                    if (fields.isEmpty()) {
                                        log.error("found custom param type[{}], but no fields parsed", paramClassFile.getName());
                                        continue;
                                    }

                                    for (MetaField field : fields) {
                                        MappedStatementParamMappingItem paramItem = new MappedStatementParamMappingItem();
                                        paramItem.setParamName(paramName + "." + field.getName());
                                        paramItem.setMappedStatementId(item.getMappedStatementId());
                                        paramItem.setDeleted(false);
                                        result.add(paramItem);
                                    }
                                }
                            }
                        }
                    }
                    result.add(item);
                }
            }
        }
        return result;
    }
}
