package io.devpl.backend.service.impl;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.devpl.backend.domain.param.JavaPojoCodeGenParam;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.backend.service.CodeGenerationService;
import io.devpl.backend.utils.Vals;
import io.devpl.codegen.template.TemplateEngine;
import jakarta.annotation.Resource;
import org.springframework.javapoet.*;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.StringWriter;

@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {

    @Resource
    TemplateEngine templateEngine;

    /**
     * 生成Java Pojo类
     *
     * @param param 参数
     * @return 生成结果
     */
    @Override
    public String generateJavaPojoClass(JavaPojoCodeGenParam param) {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(Vals.whenBlank(param.getClassName(), "Pojo"))
            .addModifiers(Modifier.PUBLIC);

        for (FieldInfo fieldInfo : param.getFields()) {
            FieldSpec.Builder fb = FieldSpec.builder(TypeName.get(String.class), fieldInfo.getFieldName(), Modifier.PRIVATE)
                .addJavadoc(Vals.whenBlank(fieldInfo.getDescription(), fieldInfo.getFieldName()));

            FieldSpec fieldSpec = fb.build();

            typeSpecBuilder.addField(fieldSpec);
        }

        // lombok注解支持
        if (param.useLombok()) {
            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(ClassName.bestGuess("lombok.Getter")).build());
            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(ClassName.bestGuess("lombok.Setter")).build());
        }

        JavaFile javaFile = JavaFile.builder(param.getPackageName(), typeSpecBuilder.build())
            .build();

        try {
            StringWriter sw = new StringWriter();
            javaFile.writeTo(sw);
            return sw.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public String generatedDtoClass(JavaPojoCodeGenParam param) {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(Vals.whenBlank(param.getClassName(), "DTO"))
            .addModifiers(Modifier.PUBLIC);

        for (FieldInfo fieldInfo : param.getFields()) {
            FieldSpec.Builder fb = FieldSpec.builder(TypeName.get(String.class), fieldInfo.getFieldName(), Modifier.PRIVATE)
                .addJavadoc(Vals.whenBlank(fieldInfo.getDescription(), fieldInfo.getFieldName()));

            AnnotationSpec annotationSpec = AnnotationSpec.builder(JsonAlias.class)
                .addMember("value", "\"%s\"", fieldInfo.getFieldKey())
                .build();

            fb.addAnnotation(annotationSpec);

            FieldSpec fieldSpec = fb.build();

            typeSpecBuilder.addField(fieldSpec);
        }

        // lombok注解支持
        if (param.useLombok()) {
            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(ClassName.bestGuess("lombok.Getter")).build());
            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(ClassName.bestGuess("lombok.Setter")).build());
        }

        JavaFile javaFile = JavaFile.builder(param.getPackageName(), typeSpecBuilder.build())
            .build();

        try {
            StringWriter sw = new StringWriter();
            javaFile.writeTo(sw);
            return sw.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
