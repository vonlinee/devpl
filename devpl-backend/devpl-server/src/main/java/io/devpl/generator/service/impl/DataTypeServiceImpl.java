package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.generator.common.PageQuery;
import io.devpl.generator.dao.DataTypeGroupMapper;
import io.devpl.generator.domain.vo.DataTypeGroupVO;
import io.devpl.generator.entity.DataTypeGroup;
import io.devpl.generator.entity.DataTypeItem;
import io.devpl.generator.service.CrudService;
import io.devpl.generator.service.IDataTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems) {
        return crudService.saveBatch(dataTypeItems);
    }

    @Override
    public boolean saveDataTypeGroup(DataTypeGroup typeGroup) {
        return crudService.saveOrUpdate(typeGroup);
    }

    @Override
    public List<DataTypeGroupVO> listDataTypeGroups() {
        return dataTypeGroupMapper.selectAllGroups();
    }

    @Override
    public Page<DataTypeItem> selectPage(PageQuery param) {
        return crudService.selectPage(DataTypeItem.class, param.getPageIndex(), param.getPageSize());
    }
}
