package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.codegen.parser.JavaParserUtils;
import io.devpl.codegen.parser.java.MetaField;
import io.devpl.generator.common.mvc.BaseServiceImpl;
import io.devpl.generator.dao.FieldInfoMapper;
import io.devpl.generator.domain.param.FieldParseParam;
import io.devpl.generator.entity.FieldInfo;
import io.devpl.generator.service.FieldInfoService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FieldInfoServiceImpl extends BaseServiceImpl<FieldInfoMapper, FieldInfo> implements FieldInfoService {

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
        }
        return fieldInfoList;
    }

    public List<FieldInfo> parseJavaFields(String content) {
        try {
            List<MetaField> metaFields = JavaParserUtils.parseFields(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }
}
