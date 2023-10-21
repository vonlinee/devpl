package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.devpl.generator.common.PageQuery;
import io.devpl.generator.dao.DataTypeGroupMapper;
import io.devpl.generator.dao.DataTypeItemMapper;
import io.devpl.generator.dao.DataTypeMappingMapper;
import io.devpl.generator.domain.vo.DataTypeGroupVO;
import io.devpl.generator.entity.DataTypeGroup;
import io.devpl.generator.entity.DataTypeItem;
import io.devpl.generator.entity.DataTypeMapping;
import io.devpl.generator.service.CrudService;
import io.devpl.generator.service.IDataTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;

/**
 * 数据类型 Service
 */
@Service
@AllArgsConstructor
public class DataTypeServiceImpl implements IDataTypeService {

    CrudService crudService;
    DataTypeGroupMapper dataTypeGroupMapper;
    DataTypeItemMapper dataTypeItemDao;
    DataTypeMappingMapper dataTypeMappingMapper;

    @Override
    public boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems) {
        return crudService.saveBatch(dataTypeItems);
    }

    @Override
    public boolean save(DataTypeItem dataTypeItem) {
        return dataTypeItemDao.insert(dataTypeItem) > 0;
    }

    @Override
    public boolean update(DataTypeItem dataTypeItem) {
        return dataTypeItemDao.updateById(dataTypeItem) > 0;
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
}
