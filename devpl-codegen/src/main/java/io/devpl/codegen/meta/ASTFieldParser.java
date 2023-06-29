package io.devpl.codegen.meta;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;

import java.util.ArrayList;
import java.util.List;

/**
 * AST 字段解析
 */
public class ASTFieldParser implements CompilationUnitVisitor<List<FieldMetaData>> {

    @Override
    public List<FieldMetaData> visit(CompilationUnit cu) {
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        List<FieldMetaData> fieldMetaDataList = new ArrayList<>();
        for (TypeDeclaration<?> type : types) {
            List<FieldDeclaration> fields = type.getFields();
            for (FieldDeclaration field : fields) {
                if (field.isStatic()) {
                    continue; // 忽略静态变量
                }
                FieldMetaData fieldMetaData = new FieldMetaData();
                final NodeList<VariableDeclarator> variables = field.getVariables();

                for (VariableDeclarator variable : variables) {
                    String fieldName = variable.getName().asString();
                    // 字段名
                    fieldMetaData.setName(fieldName);
                    // 类型名称
                    fieldMetaData.setDataTypeName(variable.getTypeAsString());
                }
                // 注释信息
                fieldMetaData.setDescription(findFieldDescription(field));
                fieldMetaDataList.add(fieldMetaData);
            }
        }
        return fieldMetaDataList;
    }

    public String findFieldDescription(FieldDeclaration field) {
        return field.getJavadocComment()
                .map(JavadocComment::parse)
                .map(Javadoc::toText)
                .orElse("");
    }
}
