package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.generator.common.PageQuery;
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

    @Override
    public int saveDataTypes(Collection<DataTypeItem> dataTypeItems) {
        boolean result = crudService.saveBatch(dataTypeItems);

        return dataTypeItems.size();
    }

    @Override
    public Page<DataTypeItem> selectPage(PageQuery param) {
        return crudService.selectPage(DataTypeItem.class, param.getPageIndex(), param.getPageSize());
    }
}
