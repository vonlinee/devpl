package io.devpl.backend.service.impl;

import io.devpl.backend.domain.param.JavaPojoCodeGenParam;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.backend.service.CodeGenerationService;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.model.FieldData;
import io.devpl.codegen.template.model.TypeData;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        TypeData model = new TypeData();
        model.setClassName(StringUtils.whenBlank(param.getClassName(), "Pojo"));
        model.setModifier(Modifier.PUBLIC.toString());
        for (FieldInfo fieldInfo : param.getFields()) {
            FieldData fieldData = new FieldData();
            fieldData.setName(fieldInfo.getFieldKey());
            fieldData.setModifier(Modifier.PRIVATE);
            fieldData.setDataType(fieldData.getDataType());
            fieldData.setComment(fieldInfo.getComment());
            model.addField(fieldData);
        }
        return templateEngine.render("codegen/templates/ext/java.pojo.vm", model);
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
            fieldData.setDataType(StringUtils.whenBlank(field.getDataType(), "String"));
            fieldData.setComment(field.getComment());
            fieldData.setModifier(Modifier.PRIVATE);
            fieldDataList.add(fieldData);
        }
        model.setFields(fieldDataList);
        return templateEngine.render("codegen/templates/ext/jackson.response.pojo.vm", model);
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
            fieldData.setDataType(StringUtils.whenBlank(field.getDataType(), "String"));
            fieldData.setComment(field.getComment());
            fieldData.setModifier(Modifier.PRIVATE);
            fieldDataList.add(fieldData);
        }
        model.setFields(fieldDataList);

        // 字段信息
        return templateEngine.render("codegen/templates/ext/easypoi.pojo.vm", model);
    }
}
