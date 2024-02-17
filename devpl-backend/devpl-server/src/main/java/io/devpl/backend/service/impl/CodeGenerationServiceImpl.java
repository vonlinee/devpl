package io.devpl.backend.service.impl;

import io.devpl.backend.domain.param.JavaPojoCodeGenParam;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.backend.service.CodeGenerationService;
import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.model.FieldData;
import io.devpl.codegen.template.model.JavaFileTemplateArguments;
import io.devpl.common.utils.Utils;
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
        JavaFileTemplateArguments model = new JavaFileTemplateArguments();
        model.addSuperInterfaces(Serializable.class);
        model.setPackageName(param.getPackageName());
        model.setClassName(StringUtils.whenBlank(param.getClassName(), "Pojo"));
        model.setModifier(Modifier.PUBLIC.toString());
        for (FieldInfo field : param.getFields()) {
            FieldData fieldData = new FieldData();
            fieldData.setName(field.getFieldName());
            fieldData.setModifier(Modifier.PRIVATE);
            fieldData.setDataType(getDataType(field));
            fieldData.setComment(Utils.removeInvisibleCharacters(field.getComment()));
            model.addField(fieldData);
        }
        return templateEngine.render("codegen/templates/ext/java.pojo.vm", model);
    }

    public String getDataType(FieldInfo fieldInfo) {
        String dataType = fieldInfo.getDataType();
        if (dataType == null) {
            return "String";
        }
        return dataType;
    }

    @Override
    public String generatedDtoClass(JavaPojoCodeGenParam param) {
        JavaFileTemplateArguments model = new JavaFileTemplateArguments();
        model.addSuperInterfaces(Serializable.class);
        model.setPackageName(param.getPackageName());
        model.setClassName(param.getClassName());

        List<FieldData> fieldDataList = new ArrayList<>();

        for (FieldInfo field : param.getFields()) {
            FieldData fieldData = new FieldData();
            fieldData.setName(field.getFieldName());
            fieldData.setDataType(getDataType(field));
            fieldData.setComment(Utils.removeInvisibleCharacters(field.getComment()));
            fieldData.setModifier(Modifier.PRIVATE);
            fieldDataList.add(fieldData);
        }
        model.setFields(fieldDataList);
        return templateEngine.render("codegen/templates/ext/jackson.response.pojo.vm", model);
    }

    @Override
    public String generatePoiPojo(JavaPojoCodeGenParam param) {
        JavaFileTemplateArguments model = new JavaFileTemplateArguments();
        model.setPackageName(param.getPackageName());
        model.addSuperInterfaces(Serializable.class);
        model.setClassName(param.getClassName());

        List<FieldData> fieldDataList = new ArrayList<>();

        for (FieldInfo field : param.getFields()) {
            FieldData fieldData = new FieldData();
            fieldData.setName(field.getFieldName());
            fieldData.setDataType(getDataType(field));
            fieldData.setComment(Utils.removeInvisibleCharacters(field.getComment()));
            fieldData.setModifier(Modifier.PRIVATE);
            fieldDataList.add(fieldData);
        }
        model.setFields(fieldDataList);
        // 字段信息
        return templateEngine.render("codegen/templates/ext/easypoi.pojo.vm", model);
    }

    /**
     * TODO 填充全局模板参数
     *
     * @param arguments 已有的模板参数
     */
    private void fillGlobalTemplateParams(TemplateArguments arguments) {
        if (arguments.isMap()) {

        }
    }
}
