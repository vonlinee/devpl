package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.generator.common.mvc.BaseServiceImpl;
import io.devpl.generator.dao.FieldInfoMapper;
import io.devpl.generator.domain.param.FieldParseParam;
import io.devpl.generator.entity.FieldInfo;
import io.devpl.generator.service.FieldInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldInfoServiceImpl extends BaseServiceImpl<FieldInfoMapper, FieldInfo> implements FieldInfoService {

    @Override
    public IPage<FieldInfo> pages(int pageIndex, int pageSize) {
        return baseMapper.selectPage(new Page<>(pageIndex, pageSize), new LambdaQueryWrapper<>());
    }

    @Override
    public List<FieldInfo> parseFields(FieldParseParam param) {
        String type = param.getType();

        return null;
    }

    public FieldInfo parseJavaFields(String content) {

    }
}
