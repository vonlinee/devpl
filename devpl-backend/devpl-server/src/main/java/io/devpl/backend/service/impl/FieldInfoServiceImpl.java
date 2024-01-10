package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.FieldInfoMapper;
import io.devpl.backend.domain.param.FieldInfoListParam;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.domain.vo.FieldParseResult;
import io.devpl.backend.entity.FieldGroup;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.backend.service.CrudService;
import io.devpl.backend.service.FieldInfoService;
import io.devpl.common.exception.FieldParseException;
import io.devpl.common.interfaces.FieldParser;
import io.devpl.common.interfaces.impl.*;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

@Slf4j
@Service
public class FieldInfoServiceImpl extends ServiceImpl<FieldInfoMapper, FieldInfo> implements FieldInfoService {

    @Resource
    CrudService crudService;

    /**
     * Java标识符正则规则
     */
    private final Pattern javaIdentifierPattern = Pattern.compile("^([a-zA-Z_$][a-zA-Z\\d_$]*)$");

    @Override
    public List<FieldInfo> listFields(FieldInfoListParam param) {
        LambdaQueryWrapper<FieldInfo> qw = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(param.getKeyword())) {
            qw.like(FieldInfo::getFieldKey, param.getKeyword());
            qw.or().like(FieldInfo::getDescription, param.getKeyword());
            qw.or().like(FieldInfo::getFieldName, param.getKeyword());
        }
        return baseMapper.selectList(qw);
    }

    @Override
    public IPage<FieldInfo> selectPage(FieldInfoListParam param) {
        String[] excludeKeys = StringUtils.split(param.getExcludedKeys(), ",");
        LambdaQueryWrapper<FieldInfo> qw = new LambdaQueryWrapper<>();
        qw.notIn(StringUtils.hasText(param.getExcludedKeys()), FieldInfo::getFieldKey, Arrays.asList(excludeKeys));
        if (!StringUtils.isBlank(param.getKeyword())) {
            qw.and(w -> {
                w.like(FieldInfo::getFieldKey, param.getKeyword());
                w.or().like(FieldInfo::getDescription, param.getKeyword());
                w.or().like(FieldInfo::getFieldName, param.getKeyword());
            });
        }
        qw.orderBy(true, false, FieldInfo::getCreateTime);
        return baseMapper.selectPage(new Page<>(param.getPageIndex(), param.getPageSize()), qw);
    }

    /**
     * 解析字段
     * TODO 完善类型推断
     *
     * @param param 参数
     * @return 字段列表
     */
    @Override
    public FieldParseResult parseFields(FieldParseParam param) throws FieldParseException {
        final String[] types = param.getType().split(">");
        final String content = param.getContent();
        FieldParser parser = FieldParser.EMPTY;
        if ("pl".equalsIgnoreCase(types[0])) {
            if ("java".equalsIgnoreCase(types[1])) {
                parser = new JavaFieldParser();
            }
        } else if ("json".equalsIgnoreCase(types[0])) {
            if ("json5".equalsIgnoreCase(types[1])) {
                parser = new Json5FieldParser();
            } else if ("json".equalsIgnoreCase(types[1])) {
                parser = new JsonFieldParser();
            }
        } else if ("html".equalsIgnoreCase(types[0])) {
            String[] columnMapping = {param.getFieldNameColumn(), param.getFieldTypeColumn(), param.getFieldDescColumn()};
            if ("table-dom".equalsIgnoreCase(types[1])) {
                parser = new HtmlTableDomFieldParser(columnMapping);
            } else if ("table-text".equalsIgnoreCase(types[1])) {
                parser = new HtmlTableContentFieldParser(columnMapping);
            }
        } else if ("sql".equalsIgnoreCase(types[0])) {
            if ("dml".equalsIgnoreCase(types[1])) {

            } else if ("qml".equalsIgnoreCase(types[1])) {
                parser = new SqlFieldParser(param.getDbType());
            }
        } else if ("other".equalsIgnoreCase(types[0])) {
            if ("url".equalsIgnoreCase(types[1])) {
                parser = new URLFieldParser();
            }
        }
        FieldParseResult result = new FieldParseResult();
        result.setFields(convertParseResultToFields(parser.parse(content)));
        return result;
    }

    private List<FieldInfo> convertParseResultToFields(List<Map<String, Object>> fields) {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        for (Map<String, Object> field : fields) {
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setFieldKey(String.valueOf(field.get(FieldParser.FIELD_NAME)));
            fieldInfo.setDataType(String.valueOf(field.get(FieldParser.FIELD_TYPE)));
            fieldInfo.setDescription(String.valueOf(field.get(FieldParser.FIELD_DESCRIPTION)));
            fieldInfoList.add(fieldInfo);
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

    @Override
    public boolean addFieldGroup(List<FieldGroup> groups) {
        return crudService.saveBatch(groups);
    }

    @Override
    public String getSampleText(String type) {
        return null;
    }
}
