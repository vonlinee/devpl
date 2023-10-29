package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.devpl.generator.common.PageQuery;
import io.devpl.generator.dao.DataTypeGroupMapper;
import io.devpl.generator.dao.DataTypeItemMapper;
import io.devpl.generator.dao.DataTypeMappingMapper;
import io.devpl.generator.domain.param.DataTypeMappingParam;
import io.devpl.generator.domain.vo.DataTypeGroupVO;
import io.devpl.generator.domain.vo.DataTypeMappingListVO;
import io.devpl.generator.domain.vo.DataTypeMappingVO;
import io.devpl.generator.entity.DataTypeGroup;
import io.devpl.generator.entity.DataTypeItem;
import io.devpl.generator.entity.DataTypeMapping;
import io.devpl.generator.service.CrudService;
import io.devpl.generator.service.DataTypeService;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;

/**
 * 数据类型 Service
 */
@Service
@AllArgsConstructor
public class DataTypeServiceImpl implements DataTypeService {

    CrudService crudService;
    DataTypeGroupMapper dataTypeGroupMapper;
    DataTypeItemMapper dataTypeItemMapper;
    DataTypeMappingMapper dataTypeMappingMapper;

    @Override
    public boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems) {
        return crudService.saveBatch(dataTypeItems);
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

    /**
     * 保存或更新数据类型分组
     *
     * @param typeGroup 数据类型分组
     * @return 是否成功
     */
    @Override
    public boolean saveDataTypeGroup(DataTypeGroup typeGroup) {
        DataTypeGroup dataTypeGroup = dataTypeGroupMapper.selectByGroupId(typeGroup.getGroupId());
        if (dataTypeGroup == null) {
            dataTypeGroupMapper.insert(typeGroup);
        } else {
            dataTypeGroup.setGroupId(typeGroup.getGroupId());
            dataTypeGroup.setGroupName(typeGroup.getGroupName());
            dataTypeGroup.setInternal(typeGroup.getInternal());
            dataTypeGroupMapper.updateById(typeGroup);
        }
        return true;
    }

    @Override
    public List<DataTypeGroupVO> listDataTypeGroups() {
        return dataTypeGroupMapper.selectAllGroups();
    }

    @Override
    public Page<DataTypeItem> selectPage(PageQuery param) {
        return crudService.selectPage(DataTypeItem.class, param.getPageIndex(), param.getPageSize());
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
        return dataTypeMappingMapper.listDataTypeMappingItems(typeId);
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
}
