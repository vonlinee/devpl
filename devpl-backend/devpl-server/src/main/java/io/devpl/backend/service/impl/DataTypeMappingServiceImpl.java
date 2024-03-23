package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import io.devpl.backend.dao.DataTypeMappingGroupMapper;
import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingAddParam;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.backend.service.DataTypeMappingService;
import io.devpl.backend.utils.BusinessUtils;
import io.devpl.sdk.util.CollectionUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DataTypeMappingServiceImpl extends ServiceImpl<DataTypeMappingMapper, DataTypeMapping> implements DataTypeMappingService {

    @Resource
    DataTypeMappingGroupMapper groupMapper;

    @Override
    public List<SelectOptionVO> listMappingGroupOptions() {
        return CollectionUtils.toList(groupMapper.listAll(), g -> new SelectOptionVO(g.getId(), g.getGroupName(), g.getId()));
    }

    @Override
    public PageInfo<DataTypeItem> listSelectablePrimaryTypes(DataTypeListParam param) {
        return BusinessUtils.startPageInfo(param, p -> baseMapper.selectUnMappedTypeList(p));
    }

    @Override
    public PageInfo<DataTypeItem> listSelectableAnotherTypes(DataTypeListParam param) {
        Set<Long> excludeTypeIds = baseMapper.selectMappedDataTypeIds(param.getGroupId(), param.getExcludeTypeId());
        param.setExcludeIds(excludeTypeIds);
        return BusinessUtils.startPageInfo(param, p -> baseMapper.selectExcludeByTypeId(param));
    }
}
