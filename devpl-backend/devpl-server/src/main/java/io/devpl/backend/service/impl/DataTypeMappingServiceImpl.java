package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.DataTypeMappingGroupMapper;
import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.backend.service.DataTypeMappingService;
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
    public List<SelectOptionVO> listSelectablePrimaryTypeOptions() {
        return CollectionUtils.toList(baseMapper.selectUnMappedTypeList(), d -> new SelectOptionVO(d.getId(), d.getLocaleTypeName(), d.getId()));
    }

    @Override
    public List<SelectOptionVO> listSelectableAnotherTypeOptions(DataTypeItem typeItem) {
        Set<Long> excludeTypeIds = baseMapper.selectMappedDataTypeIds(null, typeItem.getId());
        List<DataTypeItem> dataTypeItems = baseMapper.selectExcludeByTypeId(typeItem.getTypeGroupId(), excludeTypeIds);
        return CollectionUtils.toList(dataTypeItems, d -> new SelectOptionVO(d.getId(), d.getLocaleTypeName(), d.getId()));
    }
}
