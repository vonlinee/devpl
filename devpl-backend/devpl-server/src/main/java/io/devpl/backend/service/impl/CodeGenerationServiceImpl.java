package io.devpl.backend.service.impl;

import io.devpl.backend.domain.param.JavaPojoCodeGenParam;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.backend.service.CodeGenerationService;
import io.devpl.backend.utils.Vals;
import io.devpl.codegen.lang.LanguageMode;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.model.FieldData;
import io.devpl.codegen.template.model.TypeData;
import jakarta.annotation.Resource;
import org.springframework.javapoet.*;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        TypeData model = new TypeData();

        model.setPackageName(param.getPackageName());
        model.setClassName(param.getClassName());

        List<FieldData> fieldDataList = new ArrayList<>();

        for (FieldInfo field : param.getFields()) {
            FieldData fieldData = new FieldData();
            fieldData.setName(field.getFieldName());
            fieldData.setDataType("String");
            fieldData.setComment(field.getComment());
            fieldData.setModifier(LanguageMode.JAVA.getModifierName(java.lang.reflect.Modifier.PRIVATE));
            fieldDataList.add(fieldData);
        }
        model.setFields(fieldDataList);

        // 字段信息
        Map<String, Object> args = new HashMap<>();
        model.fill(args);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            templateEngine.render("codegen/templates/ext/jackson.response.pojo.vm", args, baos);
            return baos.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public String generatePoiPojo(JavaPojoCodeGenParam param) {
        return test(param);
    }

    private String test(JavaPojoCodeGenParam param) {
        TypeData model = new TypeData();

        model.setPackageName(param.getPackageName());

        model.setSuperClass(Serializable.class.getName());
        model.setSuperClass(HashMap.class.getName());
        model.addSuperInterfaces(Serializable.class.getName());
        model.setClassName(param.getClassName());

        List<FieldData> fieldDataList = new ArrayList<>();

        for (FieldInfo field : param.getFields()) {
            FieldData fieldData = new FieldData();
            fieldData.setName(field.getFieldName());
            fieldData.setDataType("String");
            fieldData.setComment(field.getComment());
            fieldData.setModifier(LanguageMode.JAVA.getModifierName(java.lang.reflect.Modifier.PRIVATE));
            fieldDataList.add(fieldData);
        }
        model.setFields(fieldDataList);

        // 字段信息
        Map<String, Object> args = new HashMap<>();
        model.fill(args);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            templateEngine.render("codegen/templates/ext/easypoi.pojo.vm", args, baos);
            return baos.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
