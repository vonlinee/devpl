package io.devpl.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.generator.common.PageQuery;
import io.devpl.generator.domain.vo.DataTypeGroupVO;
import io.devpl.generator.entity.DataTypeGroup;
import io.devpl.generator.entity.DataTypeItem;

import java.util.Collection;
import java.util.List;

public interface IDataTypeService {

    boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems);

    boolean save(DataTypeItem dataTypeItem);

    boolean update(DataTypeItem dataTypeItem);

    boolean saveDataTypeGroup(DataTypeGroup typeGroup);

    List<DataTypeGroupVO> listDataTypeGroups();

    Page<DataTypeItem> selectPage(PageQuery param);
}
