package io.devpl.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.generator.common.PageQuery;
import io.devpl.generator.entity.DataTypeItem;

import java.util.Collection;
import java.util.List;

public interface IDataTypeService {
    int saveDataTypes(Collection<DataTypeItem> dataTypeItems);

    Page<DataTypeItem> selectPage(PageQuery param);
}
