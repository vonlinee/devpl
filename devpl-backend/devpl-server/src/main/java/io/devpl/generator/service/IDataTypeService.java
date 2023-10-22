package io.devpl.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.generator.common.PageQuery;
import io.devpl.generator.domain.vo.DataTypeGroupVO;
import io.devpl.generator.domain.vo.DataTypeMappingListVO;
import io.devpl.generator.domain.vo.DataTypeMappingVO;
import io.devpl.generator.entity.DataTypeGroup;
import io.devpl.generator.entity.DataTypeItem;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IDataTypeService {

    boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems);

    boolean save(DataTypeItem dataTypeItem);

    boolean update(DataTypeItem dataTypeItem);

    boolean removeById(Long typeId);

    boolean saveDataTypeGroup(DataTypeGroup typeGroup);

    List<DataTypeGroupVO> listDataTypeGroups();

    Page<DataTypeItem> selectPage(PageQuery param);

    boolean addDataTypeMapping(Long typeId, Long anotherTypeId);

    void addDataTypeMapping(MultiValueMap<Long, Long> dataTypeIdMapping);

    List<DataTypeMappingListVO> listDataTypeMappings(Long typeId);

    List<DataTypeMappingVO> listAllMappableDataTypes(Long typeId);
}
