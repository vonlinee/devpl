package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.DataTypeGroupMapper;
import io.devpl.backend.entity.DataTypeGroup;
import io.devpl.backend.service.DataTypeGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTypeGroupServiceImpl extends ServiceImpl<DataTypeGroupMapper, DataTypeGroup> implements DataTypeGroupService {

    @Override
    public List<String> listAllGroupId() {
        return lambdaQuery().select(DataTypeGroup::getGroupId).list().stream().map(DataTypeGroup::getGroupId).toList();
    }
}
