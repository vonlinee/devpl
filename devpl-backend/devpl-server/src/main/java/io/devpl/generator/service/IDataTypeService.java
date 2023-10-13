package io.devpl.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.generator.common.PageQuery;
import io.devpl.generator.entity.DataTypeGroup;
import io.devpl.generator.entity.DataTypeItem;

import java.util.Collection;

public interface IDataTypeService {

    boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems);

    boolean saveDataTypeGroup(DataTypeGroup typeGroup);

    Page<DataTypeItem> selectPage(PageQuery param);
}
