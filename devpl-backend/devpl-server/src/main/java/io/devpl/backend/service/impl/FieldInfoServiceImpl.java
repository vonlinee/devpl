package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import de.marhali.json5.*;
import io.devpl.backend.dao.FieldInfoMapper;
import io.devpl.backend.domain.param.FieldInfoListParam;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.backend.interfaces.FieldParser;
import io.devpl.backend.interfaces.impl.HtmlTableContentFieldParser;
import io.devpl.backend.interfaces.impl.HtmlTableDomFieldParser;
import io.devpl.backend.service.FieldInfoService;
import io.devpl.backend.tools.parser.JavaParserUtils;
import io.devpl.backend.tools.parser.java.MetaField;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

@Slf4j
@Service
public class FieldInfoServiceImpl extends ServiceImpl<FieldInfoMapper, FieldInfo> implements FieldInfoService {

    private final Json5 json5 = new Json5(Json5Options.builder().build().remainComment(true));

    /**
     * Java标识符正则规则
     */
    private final Pattern javaIdentifierPattern = Pattern.compile("^([a-zA-Z_$][a-zA-Z\\d_$]*)$");

    @Override
    public List<FieldInfo> listFields(FieldInfoListParam param) {
        return baseMapper.selectList(new LambdaQueryWrapper<FieldInfo>().like(StringUtils.hasText(param.getFieldKey()), FieldInfo::getFieldKey, param.getFieldKey()).like(StringUtils.hasText(param.getFieldName()), FieldInfo::getFieldName, param.getFieldName()));
    }

    @Override
    public IPage<FieldInfo> selectPage(FieldInfoListParam param) {
        String[] excludeKeys = StringUtils.split(param.getExcludedKeys(), ",");
        return baseMapper.selectPage(new Page<>(param.getPageIndex(), param.getPageSize()), new LambdaQueryWrapper<FieldInfo>().notIn(StringUtils.hasText(param.getExcludedKeys()), FieldInfo::getFieldKey, Arrays.asList(excludeKeys)).like(StringUtils.hasText(param.getFieldKey()), FieldInfo::getFieldKey, param.getFieldKey()).like(StringUtils.hasText(param.getFieldName()), FieldInfo::getFieldName, param.getFieldName()));
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
        String content = param.getContent();
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        if ("java".equalsIgnoreCase(type)) {
            fieldInfoList.addAll(parseJavaFields(param.getContent()));
        } else if ("json".equalsIgnoreCase(type)) {
            fieldInfoList.addAll(parseFieldsFromJson(content));
        } else if ("html1".equalsIgnoreCase(type)) {

            String[] columnMapping = {param.getFieldNameColumn(), param.getFieldTypeColumn(), param.getFieldDescColumn()};

            HtmlTableContentFieldParser parser = new HtmlTableContentFieldParser();
            parser.setColumnMapping(columnMapping);
            List<Map<String, Object>> fields = parser.parse(content);
            for (Map<String, Object> field : fields) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldKey(String.valueOf(field.get(FieldParser.FIELD_NAME)));
                fieldInfo.setDataType(String.valueOf(field.get(FieldParser.FIELD_TYPE)));
                fieldInfo.setDescription(String.valueOf(field.get(FieldParser.FIELD_DESCRIPTION)));
                fieldInfoList.add(fieldInfo);
            }
        } else if ("html2".equalsIgnoreCase(type)) {
            String[] columnMapping = {param.getFieldNameColumn(), param.getFieldTypeColumn(), param.getFieldDescColumn()};
            FieldParser parser = new HtmlTableDomFieldParser(columnMapping);
            List<Map<String, Object>> fields = parser.parse(content);
            for (Map<String, Object> field : fields) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldKey(String.valueOf(field.get(FieldParser.FIELD_NAME)));
                fieldInfo.setDataType(String.valueOf(field.get(FieldParser.FIELD_TYPE)));
                fieldInfo.setDescription(String.valueOf(field.get(FieldParser.FIELD_DESCRIPTION)));
                fieldInfoList.add(fieldInfo);
            }
        }
        return fieldInfoList;
    }

    /**
     * TODO 校验字段名称
     *
     * @param fieldInfoList 字段信息列表
     * @return 是否成功
     */
    @Override
    public boolean saveFieldsInfos(List<FieldInfo> fieldInfoList) {
        List<String> existedKeys = listFieldKeys();
        if (!CollectionUtils.isEmpty(existedKeys)) {
            Map<String, String> existedKeyMap = CollectionUtils.toMap(existedKeys, Function.identity());
            fieldInfoList.removeIf(f -> existedKeyMap.containsKey(f.getFieldKey()));
        }
        if (fieldInfoList.isEmpty()) {
            return true;
        }
        fieldInfoList.removeIf(f -> !javaIdentifierPattern.matcher(f.getFieldKey()).matches());
        return saveBatch(fieldInfoList);
    }

    /**
     * 查询所有的字段Key标识符
     *
     * @return 所有字段Key标识符
     */
    @Override
    public List<String> listFieldKeys() {
        return baseMapper.selectFieldKeys();
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

    private List<FieldInfo> parseJavaFields(String content) {
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
