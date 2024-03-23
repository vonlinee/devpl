package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.devpl.backend.dao.DataTypeGroupMapper;
import io.devpl.backend.dao.DataTypeItemMapper;
import io.devpl.backend.dao.DataTypeMappingGroupMapper;
import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.domain.param.DataTypeGroupParam;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingAddParam;
import io.devpl.backend.domain.param.DataTypeMappingListParam;
import io.devpl.backend.domain.vo.DataTypeGroupVO;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeGroup;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.backend.service.CrudService;
import io.devpl.backend.service.DataTypeItemService;
import io.devpl.backend.service.DataTypeMappingService;
import io.devpl.sdk.annotations.NotNull;
import io.devpl.sdk.collection.Sets;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 数据类型 Service
 */
@Service
@AllArgsConstructor
public class DataTypeServiceImpl extends ServiceImpl<DataTypeItemMapper, DataTypeItem> implements DataTypeItemService {

    CrudService crudService;
    DataTypeGroupMapper dataTypeGroupMapper;
    DataTypeItemMapper dataTypeItemMapper;
    DataTypeMappingMapper dataTypeMappingMapper;
    DataTypeMappingService dataTypeMappingService;
    DataTypeMappingGroupMapper dataTypeMappingGroupMapper;

    @Override
    public boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems) {
        return this.saveOrUpdateBatch(dataTypeItems);
    }

    @Override
    public boolean saveDataType(DataTypeItem dataTypeItem) {
        return dataTypeItemMapper.insert(dataTypeItem) > 0;
    }

    @Override
    public boolean update(DataTypeItem dataTypeItem) {
        return dataTypeItemMapper.updateById(dataTypeItem) > 0;
    }

    @Override
    public boolean removeById(Long typeId) {
        return SqlHelper.retBool(dataTypeItemMapper.deleteById(typeId));
    }

    @Override
    public boolean saveOrUpdateTypeGroups(List<DataTypeGroup> dataTypeGroups) {
        for (DataTypeGroup dataTypeGroup : dataTypeGroups) {
            saveDataTypeGroup(dataTypeGroup);
        }
        return true;
    }

    /**
     * 保存或更新数据类型分组
     *
     * @param typeGroup 数据类型分组
     * @return 是否成功
     */
    @Override
    public boolean saveDataTypeGroup(DataTypeGroup typeGroup) {
        DataTypeGroup dataTypeGroup = dataTypeGroupMapper.selectById(typeGroup.getId());
        boolean res;
        if (dataTypeGroup == null) {
            typeGroup.setCreateTime(LocalDateTime.now());
            typeGroup.setUpdateTime(typeGroup.getCreateTime());
            res = dataTypeGroupMapper.insert(typeGroup) > 0;
        } else {
            dataTypeGroup.setGroupId(typeGroup.getGroupId());
            dataTypeGroup.setGroupName(typeGroup.getGroupName());
            dataTypeGroup.setInternal(typeGroup.getInternal());
            dataTypeGroup.setUpdateTime(LocalDateTime.now());
            res = dataTypeGroupMapper.updateById(typeGroup) > 0;
        }
        return res;
    }

    @Override
    public List<DataTypeGroupVO> listDataTypeGroups() {
        return dataTypeGroupMapper.selectAllGroups();
    }

    @Override
    public Page<DataTypeItem> selectPage(DataTypeListParam param) {
        LambdaQueryWrapper<DataTypeItem> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.hasText(param.getTypeGroupId()), DataTypeItem::getTypeGroupId, param.getTypeGroupId());
        qw.eq(StringUtils.hasText(param.getTypeKey()), DataTypeItem::getTypeKey, param.getTypeKey());
        qw.like(StringUtils.hasText(param.getTypeName()), DataTypeItem::getLocaleTypeName, param.getTypeName());
        qw.orderBy(true, true, DataTypeItem::getTypeGroupId);
        return baseMapper.selectPage(new Page<>(param.getPageIndex(), param.getPageSize()), qw);
    }

    /**
     * 添加数据类型映射关系
     *
     * @param param 数据类型映射关系 添加参数
     */
    @Override
    public boolean addDataTypeMapping(DataTypeMappingAddParam param) {
        if (param.getTypeId() == null || CollectionUtils.isEmpty(param.getAnotherTypeIds())) {
            return true;
        }
        param.getAnotherTypeIds().remove(param.getTypeId());
        if (param.getAnotherTypeIds().isEmpty()) {
            return true;
        }
        // 查询所有数据类型信息
        Set<Long> ids = Sets.newSet(param.getAnotherTypeIds(), param.getTypeId());
        List<DataTypeItem> dataTypeItems = dataTypeItemMapper.listByIds(ids);
        Map<Long, DataTypeItem> dataTypeItemMap = CollectionUtils.toMap(dataTypeItems, DataTypeItem::getId);

        List<DataTypeMapping> mappings = new ArrayList<>();
        for (Long anotherTypeId : param.getAnotherTypeIds()) {
            DataTypeMapping mapping = new DataTypeMapping(param.getTypeId(), anotherTypeId);
            mapping.setTypeKey(dataTypeItemMap.get(param.getTypeId()).getTypeKey());
            mapping.setAnotherTypeKey(dataTypeItemMap.get(anotherTypeId).getTypeKey());
            mapping.setGroupId(param.getGroupId());
            mappings.add(mapping);
        }

        // TODO 重复由前端过滤

        return dataTypeMappingService.saveBatch(mappings);
    }

    @Override
    public long countDataType(DataTypeMappingListParam param) {
        LambdaQueryWrapper<DataTypeItem> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.hasText(param.getTypeKeyPattern()), DataTypeItem::getTypeKey, param.getTypeKeyPattern());
        qw.eq(StringUtils.hasText(param.getTypeGroupId()), DataTypeItem::getTypeGroupId, param.getTypeGroupId());
        return baseMapper.selectCount(qw);
    }

    /**
     * 查询数据类型映射关系列表
     * 根据主类型查询列表及其映射的类型列表
     *
     * @param param 查询参数
     * @return 数据类型映射关系列表
     */
    @Override
    public List<DataTypeMappingListVO> listDataTypeMappings(DataTypeMappingListParam param) {
        return dataTypeMappingMapper.selectMappingsByPrimaryType(param);
    }

    /**
     * 查询某个类型可映射的数据类型
     *
     * @param typeId 类型ID
     * @return 可映射的数据类型列表
     */
    @Override
    public List<DataTypeMappingVO> listAllMappableDataTypes(@Nullable Long typeId) {
        if (typeId == null) {
            return dataTypeMappingMapper.listAllUnMappedDataTypes();
        }
        return dataTypeMappingMapper.listAllMappableDataTypes(typeId);
    }

    /**
     * 获取某个分组可选择的类型列表，包含名称和ID
     *
     * @param typeGroupId 类型分组，为空则获取所有
     * @return 选项列表VO
     */
    @Override
    public List<SelectOptionVO> getSelectableTypes(@NotNull String typeGroupId) {
        List<DataTypeItem> dataTypeItems = dataTypeItemMapper.listByGroupId(typeGroupId);
        List<SelectOptionVO> result = new ArrayList<>();
        for (DataTypeItem item : dataTypeItems) {
            result.add(new SelectOptionVO(item.getTypeKey(), item.getTypeGroupId() + ">" + item.getLocaleTypeName(),
                StringUtils.whenBlank(item.getFullTypeKey(), item.getTypeKey())));
        }
        return result;
    }

    /**
     * 获取类型分组下拉列表
     *
     * @return 类型分组下拉列表
     */
    @Override
    public List<SelectOptionVO> getSelectableTypeGroups(String excludeTypeGroupId) {
        List<DataTypeGroupVO> dataTypeGroupVOS = dataTypeGroupMapper.selectAllGroups();
        List<SelectOptionVO> result = new ArrayList<>();
        for (DataTypeGroupVO group : dataTypeGroupVOS) {
            if (StringUtils.hasText(excludeTypeGroupId) && Objects.equals(group.getTypeGroupId(), excludeTypeGroupId)) {
                continue;
            }
            result.add(new SelectOptionVO(group.getId(), group.getTypeGroupId(), group.getTypeGroupId()));
        }
        return result;
    }

    @Override
    public boolean removeDataTypeGroupByIds(DataTypeGroupParam param) {
        return dataTypeGroupMapper.deleteByIds(CollectionUtils.toArray(param.getGroups(), DataTypeGroup::getId, Long[]::new));
    }
}
