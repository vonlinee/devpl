package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.devpl.backend.dao.DataTypeGroupMapper;
import io.devpl.backend.dao.DataTypeItemMapper;
import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingParam;
import io.devpl.backend.domain.vo.DataTypeGroupVO;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeGroup;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.backend.service.CrudService;
import io.devpl.backend.service.DataTypeItemService;
import io.devpl.sdk.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Override
    public boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems) {
        return crudService.saveOrUpdateBatch(dataTypeItems);
    }

    @Override
    public boolean save(DataTypeItem dataTypeItem) {
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
        if (dataTypeGroup == null) {
            typeGroup.setCreateTime(LocalDateTime.now());
            typeGroup.setUpdateTime(typeGroup.getCreateTime());
            dataTypeGroupMapper.insert(typeGroup);
        } else {
            dataTypeGroup.setGroupId(typeGroup.getGroupId());
            dataTypeGroup.setGroupName(typeGroup.getGroupName());
            dataTypeGroup.setInternal(typeGroup.getInternal());
            dataTypeGroup.setUpdateTime(LocalDateTime.now());
            dataTypeGroupMapper.updateById(typeGroup);
        }
        return true;
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
        qw.like(StringUtils.hasText(param.getTypeName()), DataTypeItem::getTypeName, param.getTypeName());
        qw.orderBy(true, true, DataTypeItem::getTypeGroupId);
        return baseMapper.selectPage(new Page<>(param.getPageIndex(), param.getPageSize()), qw);
    }

    /**
     * 添加数据类型映射关系
     *
     * @param typeId        数据类型ID
     * @param anotherTypeId 另一个数据类型ID
     */
    @Override
    public boolean addDataTypeMapping(Long typeId, Long anotherTypeId) {
        DataTypeMapping dataTypeMapping = new DataTypeMapping(typeId, anotherTypeId);
        return SqlHelper.retBool(dataTypeMappingMapper.insert(dataTypeMapping));
    }

    @Override
    public boolean addDataTypeMapping(List<DataTypeMappingParam> params) {
        return crudService.saveBatch(params.stream()
            .map(i -> new DataTypeMapping(i.getTypeId(), i.getAnotherTypeId()))
            .toList());
    }

    /**
     * 添加数据类型映射关系
     *
     * @param dataTypeIdMapping 数据类型ID映射关系
     */
    @Override
    public void addDataTypeMapping(MultiValueMap<Long, Long> dataTypeIdMapping) {
        // TODO 待完成
        List<DataTypeMapping> existedTypeMappings = dataTypeMappingMapper.selectListByIds(dataTypeIdMapping.keySet());
    }

    @Override
    public List<DataTypeMappingListVO> listDataTypeMappings(Long typeId) {
        return dataTypeMappingMapper.listDataTypeMappings(typeId);
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

    @Override
    public List<SelectOptionVO> getSelectableTypes(String typeGroup) {
        List<DataTypeItem> dataTypeItems = dataTypeItemMapper.listByGroupId(typeGroup);
        List<SelectOptionVO> result = new ArrayList<>();
        for (DataTypeItem item : dataTypeItems) {
            result.add(new SelectOptionVO(item.getTypeKey(), item.getTypeGroupId() + item.getTypeName(), item.getTypeKey()));
        }
        return result;
    }

    @Override
    public List<SelectOptionVO> getSelectableTypeGroups() {
        List<DataTypeGroupVO> dataTypeGroupVOS = dataTypeGroupMapper.selectAllGroups();
        List<SelectOptionVO> result = new ArrayList<>();
        for (DataTypeGroupVO group : dataTypeGroupVOS) {
            result.add(new SelectOptionVO(group.getId(), group.getTypeGroupId(), group.getTypeGroupId()));
        }
        return result;
    }
}
