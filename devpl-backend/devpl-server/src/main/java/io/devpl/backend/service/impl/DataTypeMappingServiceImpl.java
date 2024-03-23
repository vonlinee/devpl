package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.pagehelper.PageInfo;
import io.devpl.backend.dao.DataTypeMappingGroupMapper;
import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.backend.entity.DataTypeMappingGroup;
import io.devpl.backend.service.DataTypeMappingService;
import io.devpl.backend.utils.BusinessUtils;
import io.devpl.sdk.util.CollectionUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<DataTypeMapping> listByGroupId(Long groupId) {
        LambdaQueryWrapper<DataTypeMapping> qw = new LambdaQueryWrapper<>();
        qw.eq(DataTypeMapping::getGroupId, groupId);
        return list(qw);
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

    /**
     * 新增数据类型分组
     * 如果组ID存在，表示将该组复制一份
     *
     * @param group 分组
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTypeMappingGroup(DataTypeMappingGroup group) {
        int count;
        if (group.getId() == null) {
            count = groupMapper.insert(group);
        } else {
            // 将该分组的所有数据类型映射复制一份
            count = groupMapper.insert(group);
            if (group.getId() != null) {
                List<DataTypeMapping> mappings = this.listByGroupId(group.getId());
                for (DataTypeMapping mapping : mappings) {
                    mapping.setId(null);
                    mapping.setGroupId(group.getId());
                }
                this.saveBatch(mappings);
            }
        }
        return SqlHelper.retBool(count);
    }
}
