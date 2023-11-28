package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import de.marhali.json5.*;
import io.devpl.generator.common.mvc.BaseServiceImpl;
import io.devpl.generator.dao.FieldInfoMapper;
import io.devpl.generator.domain.param.FieldParseParam;
import io.devpl.generator.entity.FieldInfo;
import io.devpl.generator.service.FieldInfoService;
import io.devpl.generator.tools.parser.JavaParserUtils;
import io.devpl.generator.tools.parser.java.MetaField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FieldInfoServiceImpl extends BaseServiceImpl<FieldInfoMapper, FieldInfo> implements FieldInfoService {

    private final Json5 json5 = new Json5(Json5Options.builder().build().remainComment(true));

    @Override
    public IPage<FieldInfo> selectPage(int pageIndex, int pageSize) {
        return baseMapper.selectPage(new Page<>(pageIndex, pageSize), new LambdaQueryWrapper<>());
    }

    /**
     * 解析字段
     *
     * @param param 参数
     * @return 字段列表
     */
    @Override
    public List<FieldInfo> parseFields(FieldParseParam param) {
        String type = param.getType();
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        if ("java".equalsIgnoreCase(type)) {
            fieldInfoList.addAll(parseJavaFields(param.getContent()));
        } else if ("json".equalsIgnoreCase(type)) {
            fieldInfoList.addAll(parseFieldsFromJson(param.getContent()));
        }
        return fieldInfoList;
    }

    /**
     * 仅支持单层JSON对象
     *
     * @param json JSON字符串，支持JSON5格式
     * @return 字段列表
     */
    public List<FieldInfo> parseFieldsFromJson(String json) {
        try {
            List<FieldInfo> res = new ArrayList<>();
            Json5Element root = json5.parse(json);
            if (root.isJson5Object()) {
                Json5Object obj = root.getAsJson5Object();
                for (Map.Entry<String, Json5Element> entry : obj.entrySet()) {
                    FieldInfo fieldInfo = new FieldInfo();
                    fieldInfo.setFieldKey(entry.getKey());

                    Json5Element value = entry.getValue();

                    if (value.isJson5Primitive()) {
                        Json5Primitive primitive = value.getAsJson5Primitive();
                        if (primitive.isBoolean()) {
                            fieldInfo.setDataType("Boolean");
                        } else if (primitive.isString()) {
                            fieldInfo.setDataType("String");
                        } else if (primitive.isNumber()) {
                            fieldInfo.setDataType("Number");
                        }
                    }
                    fieldInfo.setDefaultValue(value.toString());
                    Json5Comment comment = obj.getComment(entry.getKey());
                    if (comment != null) {
                        fieldInfo.setDescription(comment.getText());
                    }
                    res.add(fieldInfo);
                }
            }
            return res;
        } catch (Exception exception) {
            log.error("[字段解析 JSON] 解析失败", exception);
        }
        return Collections.emptyList();
    }

    public List<FieldInfo> parseJavaFields(String content) {
        try {
            List<MetaField> metaFields = JavaParserUtils.parseFields(content);
            List<FieldInfo> res = new ArrayList<>();
            for (MetaField metaField : metaFields) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldKey(metaField.getIdentifier());
                fieldInfo.setFieldName(metaField.getName());
                fieldInfo.setDescription(metaField.getDescription());
                fieldInfo.setDataType(metaField.getDataType());
                res.add(fieldInfo);
            }
            return res;
        } catch (IOException e) {
            log.error("[字段解析 JAVA] 解析失败", e);
        }
        return Collections.emptyList();
    }
}
