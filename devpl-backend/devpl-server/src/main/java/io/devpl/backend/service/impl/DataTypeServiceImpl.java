package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.devpl.backend.dao.DataTypeGroupMapper;
import io.devpl.backend.dao.DataTypeItemMapper;
import io.devpl.backend.dao.DataTypeMappingGroupMapper;
import io.devpl.backend.domain.param.DataTypeGroupParam;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingAddParam;
import io.devpl.backend.domain.param.DataTypeMappingListParam;
import io.devpl.backend.domain.vo.*;
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
        // 兼容性处理
        if (param.getTypeId() == null || CollectionUtils.isEmpty(param.getAnotherTypeIds())) {
            return true;
        }
        // 避免相同的类型进行映射
        param.getAnotherTypeIds().remove(param.getTypeId());
        // 去重
        List<Long> mappedTypeIds = dataTypeMappingService.listMappedDataTypeId(param.getGroupId(), param.getTypeId());
        param.getAnotherTypeIds().removeAll(mappedTypeIds);
        if (param.getAnotherTypeIds().isEmpty()) {
            return true;
        }
        // 查询所有数据类型信息
        Set<Long> ids = Sets.newSet(param.getAnotherTypeIds(), param.getTypeId());
        Map<Long, DataTypeItem> dataTypeItemMap = CollectionUtils.toMap(dataTypeItemMapper.listByIds(ids), DataTypeItem::getId);
        if (!dataTypeItemMap.containsKey(param.getTypeId())) {
            throw new RuntimeException("主数据类型不存在");
        }
        List<DataTypeMapping> mappings = new ArrayList<>();
        for (Long anotherTypeId : param.getAnotherTypeIds()) {
            DataTypeMapping mapping = new DataTypeMapping(param.getTypeId(), anotherTypeId);
            mapping.setTypeKey(dataTypeItemMap.get(param.getTypeId()).getTypeKey());
            mapping.setAnotherTypeKey(dataTypeItemMap.get(anotherTypeId).getTypeKey());
            mapping.setGroupId(param.getGroupId());
            mappings.add(mapping);
        }
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
        return dataTypeMappingService.selectMappingsByPrimaryType(param);
    }

    @Override
    public DataTypeMappingByTypeGroup getDataTypeMappingsByGroup(DataTypeMappingListParam param) {
        DataTypeMappingByTypeGroup vo = new DataTypeMappingByTypeGroup();
        vo.setTypeGroupId(param.getTypeGroupId());
        vo.setAnotherTypeGroupId(param.getAnotherTypeGroupId());
        List<DataTypeMapping> mappings = dataTypeMappingService.listByTypeGroupKey(param.getGroupId(), param.getTypeGroupId(), param.getAnotherTypeGroupId());
        if (CollectionUtils.isEmpty(mappings)) {
            return vo;
        }
        // 按主类型分组
        Map<Long, List<DataTypeMapping>> groupByTypeId = CollectionUtils.groupingBy(mappings, DataTypeMapping::getTypeId);
        // 映射类型的ID
        Set<Long> anotherTypeIds = CollectionUtils.toSet(mappings, DataTypeMapping::getAnotherTypeId);

        List<Long> typeIds = CollectionUtils.addAll(new ArrayList<>(), groupByTypeId.keySet(), anotherTypeIds);

        List<DataTypeItem> dataTypeItems = listByIds(typeIds);

        Map<Long, DataTypeItem> typeItemMap = CollectionUtils.toMap(dataTypeItems, DataTypeItem::getId);

        List<DataTypeItem> primaryTypeList = new ArrayList<>();
        List<Collection<DataTypeItem>> mappedTypesList = new ArrayList<>();
        for (Map.Entry<Long, List<DataTypeMapping>> entry : groupByTypeId.entrySet()) {
            Set<Long> set = CollectionUtils.toSet(entry.getValue(), DataTypeMapping::getAnotherTypeId);
            Collection<DataTypeItem> mappedTypes = CollectionUtils.values(typeItemMap, set);
            primaryTypeList.add(typeItemMap.get(entry.getKey()));
            mappedTypesList.add(mappedTypes);
        }
        vo.setTypes(CollectionUtils.values(typeItemMap, groupByTypeId.keySet()));
        vo.setTypes(primaryTypeList);
        vo.setMappedDataTypes(mappedTypesList);
        return vo;
    }

    /**
     * 查询某个类型可映射的数据类型
     *
     * @param typeId 类型ID
     * @return 可映射的数据类型列表
     */
    @Override
    public List<DataTypeMappingVO> listMappableDataTypes(@Nullable Long typeId) {
        if (typeId == null) {
            return dataTypeMappingService.listAllUnMappedDataTypes();
        }
        return dataTypeMappingService.listAllMappableDataTypes(typeId);
    }

    @Override
    public List<MappedDataTypeVO> listMappableDataTypes(@NotNull Long groupId, @NotNull Long typeId, @NotNull String anotherTypeGroup) {
        return dataTypeMappingService.listMappableDataTypes(groupId, typeId, anotherTypeGroup);
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
            result.add(new SelectOptionVO(item.getId(), item.getTypeKey(),
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
        Long[] ids = param.getGroups().stream().map(DataTypeGroup::getId).toArray(Long[]::new);
        return dataTypeGroupMapper.deleteByIds(ids);
    }

    @Override
    public List<DataTypeItem> listByGroupId(String groupId) {
        return dataTypeItemMapper.listByGroupId(groupId);
    }
}
