package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.dao.FieldGroupMapper;
import io.devpl.backend.domain.param.FieldGroupListParam;
import io.devpl.backend.entity.FieldGroup;
import io.devpl.backend.service.FieldGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldGroupServiceImpl implements FieldGroupService {

    @Resource
    FieldGroupMapper fieldGroupMapper;

    @Override
    public List<FieldGroup> listFieldGroups(FieldGroupListParam param) {
        LambdaQueryWrapper<FieldGroup> qw = new LambdaQueryWrapper<>();
        return fieldGroupMapper.selectList(qw);
    }

    @Override
    public IPage<FieldGroup> listPage(FieldGroupListParam param) {
        LambdaQueryWrapper<FieldGroup> qw = new LambdaQueryWrapper<>();
        return fieldGroupMapper.selectPage(param.asPage(), qw);
    }
}
