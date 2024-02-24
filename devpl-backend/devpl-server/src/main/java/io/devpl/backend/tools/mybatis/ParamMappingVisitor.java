package io.devpl.backend.tools.mybatis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;
import io.devpl.backend.entity.MappedStatementParamMappingItem;
import io.devpl.codegen.parser.java.CompilationUnitVisitor;
import io.devpl.sdk.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParamMappingVisitor implements CompilationUnitVisitor<List<MappedStatementParamMappingItem>> {

    private File mapperFile;

    public ParamMappingVisitor() {
    }

    public ParamMappingVisitor(File file) {
        this.mapperFile = file;
    }

    public File getMapperFile() {
        return mapperFile;
    }

    public void setMapperFile(File mapperFile) {
        this.mapperFile = mapperFile;
    }

    @Override
    public List<MappedStatementParamMappingItem> visit(CompilationUnit cu) {

        List<MappedStatementParamMappingItem> result = new ArrayList<>();

        String packageName = cu.getPackageDeclaration().map(PackageDeclaration::getName).map(Name::asString).orElse(null);

        for (TypeDeclaration<?> type : cu.getTypes()) {
            String mapperClassName = type.getNameAsString();

            for (MethodDeclaration method : type.getMethods()) {

                String mapperMethodName = method.getNameAsString();

                String msId = String.join(".", packageName, mapperClassName, mapperMethodName);

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

                    if (StringUtils.equalsAny(item.getParamType(), false,
                        "Int", "Long", "String", "Double", "Float", "Short", "Byte", "Collection")) {

                    }

                    System.out.println(item.getParamType());

                    // 如果是对象类型 则解析该对象的字段
                    result.add(item);
                }
            }
        }

        return result;
    }
}
